import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiPower, FiTrash2, FiEdit3, FiArrowLeft } from "react-icons/fi";

import logoImg from "../../../assets/images/logo.png";
import api from "../../../services/api";
import "./styles.css";

export default function Reservations() {
  const [reservations, setReservations] = useState([]);

  const history = useHistory();

  const token = sessionStorage.getItem("token");

  useEffect(() => {
    api
      .get("api/reservations/", {
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
    if (window.confirm("Tem certeza que deseja deletar esta reserva?")) {
      try {
        api.delete(`api/reservations/${id}`, {
          headers: {
            Authorization: "Bearer " + token,
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
          },
        });
        alert("Reserva deletada com sucesso!");
        history.go(0);
      } catch (err) {
        alert("Erro ao deletar caso, tente novamente.");
      }
    }
  }

  return (
    <div className="profile-container">
      <header>
        <img src={logoImg} alt="Logo" />
        <span>Bem-vindo ao Hostel</span>

        <Link className="button" to="/reservations/newReservation">
          Cadastrar nova reserva
        </Link>
        <button type="button"
          onClick={() => {
            sessionStorage.clear();
            window.location.reload();
          }}
        >
          <FiPower size={18} color="#E02041" />
        </button>
      </header>

      <div className="reservations-grid">
        <h1>Reservas cadastradas</h1>

        <ul>
          {reservations.map(
            (
              { id, guestName, guest_ID, rooms, checkinDate, checkoutDate, payment },
              i
            ) => (
              <li key={id}>
                <strong>HÓSPEDE: {guestName}</strong>
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
        <Link className="back-link" to="/admin/profile">
          <FiArrowLeft size={16} color="#E02041" />
          Voltar
        </Link>
      </div>
    </div>
  );
}
