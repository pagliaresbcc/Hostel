import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import * as Yup from "yup";

import "./styles.css";

import logoImg from "../../assets/images/logo.png";

export default function NewReservation() {
  const token = sessionStorage.getItem("token");

  const [checkinDate, setCheckinDate] = useState(new Date());
  const [checkoutDate, setCheckoutDate] = useState(new Date());
  const [numberOfGuests, setNumberOfGuests] = useState(0);
  const [minDailyRate, setMinDailyRate] = useState("");
  const [maxDailyRate, setMaxDailyRate] = useState("");

  const history = useHistory();

  const validation = Yup.object().shape({
    checkinDate: Yup.date().min(
      new Date(),
      "A data de check-in deve ser maior que a data de hoje!"
    ),
    checkoutDate: Yup.date().min(
      checkinDate,
      "A data de check-out deve ser maior que a data de check-in!"
    ),
    numberOfGuests: Yup.number().min(
      1,
      "O número de hóspedes deve ser maior que 0"
    ),
  });

  function handleRegister(e) {
    e.preventDefault();

    validation
      .validate({
        checkinDate,
        checkoutDate,
        numberOfGuests,
      })
      .then(() => {
        sessionStorage.setItem("checkinDate", checkinDate);
        sessionStorage.setItem("checkoutDate", checkoutDate);
        sessionStorage.setItem("numberOfGuests", numberOfGuests);

        history.push("/rooms/selectAvailableRooms");
      })
      .catch(function (err) {
        err.errors.forEach((error) => {
          alert(`${error}`);
        });
      });
  }

  if (token === null) {
    history.push("/");
    return <div></div>;
  } else {
    return (
      <div className="nova-reserva-container">
        <div className="content">
          <section>
            <img src={logoImg} alt="Logo" />

            <h1>Cadastrar nova reserva</h1>
            <p>Agende sua nova reserva para a data mais próxima</p>

            <Link className="back-link" to="/profile">
              <FiArrowLeft size={16} color="#E02041" />
              Voltar
            </Link>
          </section>

          <form onSubmit={handleRegister}>
            <label>Check-in</label>
            <input
              id="check-in"
              required="true"
              type="date"
              onChange={(e) => setCheckinDate(e.target.value)}
            />
            <label>Check-out</label>
            <input
              id="check-out"
              required="true"
              type="date"
              onChange={(e) => setCheckoutDate(e.target.value)}
            />
            <label>Número de hóspedes</label>
            <input
              id="numberOfGuests"
              required="true"
              type="number"
              value={numberOfGuests}
              onChange={(e) => setNumberOfGuests(e.target.value)}
            />
            <label>Valor mínimo</label>
            <input
              id="minDailyRate"
              value={minDailyRate}
              onChange={(e) => setMinDailyRate(e.target.value)}
            />
            <label>Valor máximo</label>
            <input
              id="maxDailyRate"
              value={maxDailyRate}
              onChange={(e) => setMaxDailyRate(e.target.value)}
            />
            <button className="button" type="submit">
              Selecionar quarto(s)
            </button>
          </form>
        </div>
      </div>
    );
  }
}
