import React, { useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import * as Yup from "yup";

import api from "../../../services/api";

import logoImg from "../../../assets/images/logo.png";

export default function UpdateReservation() {
  const [checkinDate, setCheckinDate] = useState(new Date());
  const [checkoutDate, setCheckoutDate] = useState(new Date());
  const [numberOfGuests, setNumberOfGuests] = useState();

  const token = sessionStorage.getItem("token");

  const reservation_ID = sessionStorage.getItem("reservation_ID");

  useEffect(() => {
    api
      .get(`api/reservations/${reservation_ID}`, {
        headers: { Authorization: "Bearer " + token },
      })
      .then((response) => {
        setCheckinDate(response.data.checkinDate);
        setCheckoutDate(response.data.checkoutDate);
        setNumberOfGuests(response.data.numberOfGuests);
      });
  }, []);


  const history = useHistory();

  const validation = Yup.object().shape({
    checkinDate: Yup.date().min(
      new Date(),
      "A data do check-in deve ser maior que a data de hoje!"
    ),
    checkoutDate: Yup.date().min(
      new Date(checkinDate),
      "A data do check-out deve ser maior que a data de check-in!"
    ),
    numberOfGuests: Yup.number().min(
      1,
      "O número de hóspedes deve ser maior que 0"
    ),
  });

  function handleUpdate(e) {
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

        history.push("/guest/update-selected-rooms");
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

          <h1>Atualizar reserva</h1>
          <p>Atualize sua reserva</p>

          <Link className="back-link" to="/admin/profile">
            <FiArrowLeft size={16} color="#E02041" />
            Voltar
          </Link>
        </section>

        <form onSubmit={handleUpdate}>
        <label>Check-in</label>
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
          <button className="button" type="submit">
            Selecionar quarto(s)
          </button>
        </form>
      </div>
    </div>
  );
}
