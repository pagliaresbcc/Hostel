import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiPower, FiTrash2, FiEdit3 } from "react-icons/fi";

import "./styles.css";

import logoImg from "../../../assets/images/logo.png";
import api from "../../../services/api";

export default function GuestProfile() {
  const [reservations, setReservations] = useState([]);
  const [guest, setGuest] = useState("");

  const history = useHistory();

  const token = sessionStorage.getItem("token");

  const guest_ID = sessionStorage.getItem("guest_ID");

  useEffect(() => {
    api
      .get(`api/guests/${guest_ID}`, {
        headers: { Authorization: "Bearer " + token },
      })
      .then((response) => {
        setGuest(response.data);
      });
  }, [token]);

  useEffect(() => {
    api
      .get(`api/reservations?guestId=${guest_ID}`, {
        headers: { Authorization: "Bearer " + token },
      })
      .then((response) => {
        setReservations(response.data);
      });
  }, [token]);

  async function handleUpdateReservation(id) {
    sessionStorage.setItem("reservation_ID", id);

    history.push("/reservations/update-reservation");
  }

  async function handleDeleteReservation(id) {
    if (window.confirm("Tem certeza de que quer deletar a reserva?")) {
      try {
        api.delete(`api/reservations/${id}`, {
          headers: {
            Authorization: "Bearer " + token,
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
          },
        });
        history.go(0);
      } catch (err) {
        alert("Erro ao deletar caso, tente novamente.");
      }
    }
  }

  if (token === null) {
    history.push("/");
    return <div></div>;
  } else {
    return (
      <div className="profile-container">
        <header>
          <img src={logoImg} alt="Logo" />
          <span>Olá {guest.name}, bem-vindo ao Hostel!</span>

          <Link
            className="button"
            onClick={() => {
              sessionStorage.setItem("guest_ID", guest_ID);
            }}
            to="/guest/update"
          >
            Editar perfil
          </Link>
          <button
            type="button"
            onClick={() => {
              sessionStorage.clear();
              window.location.reload();
            }}
          >
            <FiPower size={18} color="#E02041" />
          </button>
        </header>

        {reservations.length === 0 ? (
          <div className="welcome-reservations-grid">
            <h1>Você ainda não cadastrou nenhuma reserva!</h1>
            <Link
              className="button"
              to="/reservations/newReservation"
              onClick={sessionStorage.setItem("guestName", guest.name)}
            >
              Cadastrar nova reserva
            </Link>
          </div>
        ) : (
          <div className="reservations-grid">
            <div className="rooms-header-label">
              <h1>Suas reservas cadastradas</h1>
              <Link className="button" to="/reservations/newReservation">
                Cadastrar nova reserva
              </Link>
            </div>
            <ul>
              {reservations.map(
                ({ id, rooms, checkinDate, checkoutDate, payment }, i) => (
                  <li key={id}>
                    <strong>QUARTO(S) RESERVADO(S):</strong>
                    {rooms.map((room, j) => (
                      <div>
                        <p>Número do quarto: {room.number}</p>
                        <p>Descrição: {room.description}</p>
                        <p>Diária: R$ {room.dailyRate.price},00</p>
                        <br />
                      </div>
                    ))}
                    <br />
                    <strong>CHECKIN:</strong>
                    <p>{checkinDate}</p>

                    <strong>CHECKOUT:</strong>
                    <p>{checkoutDate}</p>

                    <strong>VALOR TOTAL:</strong>
                    <p>
                      R${" "}
                      {payment.type === "cash"
                        ? payment.amountTendered
                        : payment.amount}
                      ,00
                    </p>

                    <button
                      className="deleteButton"
                      onClick={() => handleDeleteReservation(id)}
                      type="button"
                    >
                      <FiTrash2 size={20} color="#a8a8b3" />
                    </button>

                    <button
                      className="editButton"
                      onClick={() => handleUpdateReservation(id)}
                      type="button"
                    >
                      <FiEdit3 size={20} color="#a8a8b3" />
                    </button>
                  </li>
                )
              )}
            </ul>
          </div>
        )}
      </div>
    );
  }
}
