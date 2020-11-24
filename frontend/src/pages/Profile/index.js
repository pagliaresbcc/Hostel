import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiPower, FiTrash2, FiEdit3 } from "react-icons/fi";

import "./styles.css";

import logoImg from "../../assets/images/logo.png";
import api from "../../services/api";

export default function Profile() {
  const [reservations, setReservations] = useState([]);

  const history = useHistory();

  const token = sessionStorage.getItem("token");

  useEffect(() => {
    var customer_ID = sessionStorage.getItem("customer_ID");
    api
      .get(`api/reservations/${customer_ID}`, {
        headers: { Authorization: "Bearer " + token },
      })
      .then((response) => {
        setReservations(response.data);
      });
  }, [token]);

  async function handleUpdateReservation(id) {
    sessionStorage.setItem("reservation_id", id);
    console.log(id)

    history.push("/reservations/updateReservation");
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

  return (
    <div className="profile-container">
      <header>
        <img src={logoImg} alt="Logo" />
        <span>Olá Hóspede, bem-vindo ao Hostel!</span>

        <Link className="button" to="/reservations/newReservation">
          Cadastrar nova reserva
        </Link>
        <button type="button">
          <FiPower size={18} color="#E02041" />
        </button>
      </header>

      {reservations.length === 0 ? (
        <h1>Você ainda não cadastrou nenhuma reserva!</h1>
      ) : (
        <div className="reservations-grid">
          <h1>Suas reservas cadastradas</h1>

          <ul>
            {reservations.map(
              ({ id, rooms, checkinDate, checkoutDate, payments }, i) => (
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
                    {payments.type === "cash"
                      ? payments.amountTendered
                      : payments.amount}
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
