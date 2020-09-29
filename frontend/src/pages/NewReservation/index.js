import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import * as Yup from "yup";

import "./styles.css";

import logoImg from "../../assets/images/logo.png";

export default function NewReservation() {
  const [checkinDate, setCheckinDate] = useState("");
  const [checkoutDate, setCheckoutDate] = useState("");
  const [numberOfGuests, setNumberOfGuests] = useState("");
  const [minDailyRate, setMinDailyRate] = useState("");
  const [maxDailyRate, setMaxDailyRate] = useState("");

  const history = useHistory();

  const validation = Yup.object().shape({
    checkinDate: Yup.date().min(
      new Date(),
      "A data do check-in deve ser maior que a data de hoje!"
    ),
    checkoutDate: Yup.date()
      .min(new Date(), "A data do check-out deve ser maior que a data de hoje!"),
    numberOfGuests: Yup.number()
      .positive()
      .min(1, "O número de hóspedes deve ser maior que 0"),
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
        localStorage.setItem("checkinDate", checkinDate);
        localStorage.setItem("checkoutDate", checkoutDate);
        localStorage.setItem("numberOfGuests", numberOfGuests);

        history.push("/room/selectAvailableRooms");
      })
      .catch(function (err) {
        err.errors.forEach((error) => {
          alert(`${error}`);
        });
      });
  }

  return (
    <div className="nova-reserva-container">
      <div className="content">
        <section>
          <img src={logoImg} alt="Logo" />

          <h1>Cadastrar nova reserva</h1>
          <p>Agende sua nova reserva para a data mais próxima</p>

          <Link className="back-link" to="/">
            <FiArrowLeft size={16} color="#E02041" />
            Não tenho cadastro
          </Link>
        </section>

        <form onSubmit={handleRegister}>
          <input
            id="check-in"
            required="true"
            type="date"
            placeholder="Check-in"
            value={checkinDate}
            onChange={(e) => setCheckinDate(e.target.value)}
          />
          <label>Check-out</label>
          <input
            id="check-out"
            required="true"
            type="date"
            placeholder="Check-out"
            value={checkoutDate}
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
