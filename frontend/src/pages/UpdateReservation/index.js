import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import api from "../../services/api";
import "./styles.css";

import logoImg from "../../assets/images/logo.png";

export default function UpdateReservation() {
  const [checkinDate, setCheckinDate] = useState("");
  const [checkoutDate, setCheckoutDate] = useState("");
  const [numberOfGuests, setNumberOfGuests] = useState("");

  const token = localStorage.getItem("token");

  const history = useHistory();

  async function handleRegister(e) {
    e.preventDefault();


    try {

      localStorage.setItem('checkinDate', checkinDate);
      localStorage.setItem('checkoutDate', checkoutDate);
      localStorage.setItem('numberOfGuests', numberOfGuests);

      console.log(checkinDate)
      console.log(checkoutDate)
      console.log(numberOfGuests)

      if (checkinDate !== null && checkoutDate !== "") {
        await api.get("api/rooms", {
          headers: { Authorization: "Bearer " + token },
          params: {
            checkinDate,
            checkoutDate,
            numberOfGuests,
          }
        });
  
        history.push("/room/updateAvailableRooms");
      } else {
        history.push("/room/selectAvailableRooms");
      }

      
    } catch (err) {
      alert('Nenhum campo foi alterado!');
      history.push("/profile");
    }
  }

  return (
    <div className="nova-reserva-container">
      <div className="content">
        <section>
          <img src={logoImg} alt="Logo" />

          <h1>Editar reserva</h1>
          <p>Altera sua reserva para melhor te atendermos!</p>

          <Link className="back-link" to="/">
            <FiArrowLeft size={16} color="#E02041" />
            Não tenho cadastro
          </Link>
        </section>

        <form onSubmit={handleRegister}>
          <label> Check-in</label>
          <input
            id="check-in"
            type="date"
            placeholder="Check-in"
            value={checkinDate}
            onChange={(e) => setCheckinDate(e.target.value)}
          />
          <label>Check-out</label>
          <input
            id="check-out"
            type="date"
            placeholder="Check-out"
            value={checkoutDate}
            onChange={(e) => setCheckoutDate(e.target.value)}
          />
          <label>Número de hóspedes</label>
          <input
            id="numberOfGuests"
            onChange={(e) => setNumberOfGuests(e.target.value)}
          />
          <button className="button" type="submit">
            Continuar com a alteração
          </button>
        </form>
      </div>
    </div>
  );
}
