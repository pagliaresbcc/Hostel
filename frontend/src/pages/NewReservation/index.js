import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import * as Yup from "yup";
import { Formik, Field, Form, ErrorMessage } from "formik";

import api from "../../services/api";
import "./styles.css";

import logoImg from "../../assets/images/logo.png";

export default function NewReservation() {
  const [checkinDate, setCheckinDate] = useState("");
  const [checkoutDate, setCheckoutDate] = useState("");
  const [numberOfGuests, setNumberOfGuests] = useState("");
  const [minDailyRate, setMinDailyRate] = useState("");
  const [maxDailyRate, setMaxDailyRate] = useState("");

  const token = localStorage.getItem("token");

  const history = useHistory();

  async function handleRegister(e) {
    console.log(checkinDate);
    console.log(checkoutDate);
    e.preventDefault();

    try {
      localStorage.setItem("checkinDate", checkinDate);
      localStorage.setItem("checkoutDate", checkoutDate);
      localStorage.setItem("numberOfGuests", numberOfGuests);

      await api.get("api/rooms", {
        headers: { Authorization: "Bearer " + token },
        params: {
          checkinDate,
          checkoutDate,
          numberOfGuests,
          minDailyRate,
          maxDailyRate,
        },
      });

      history.push("/room/selectAvailableRooms");
    } catch (err) {
      alert("Erro nas informações, tente novamente");
    }
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

        <Formik validationSchema={Yup.object().shape({
              checkinDate: Yup.date()
                .required()
                .min(new Date(2020, 9, 29), "AMIGA SUA LOKA"),
              checkoutDate: Yup.date().required(),
              numberOfGuests: Yup.number().required(),
            })}>
          <Form
            onSubmit={handleRegister}
          >
            <label> Check-in</label>
            <Field
              name="checkinDate"
              type="date"
              placeholder="Check-in"
              value={checkinDate}
              onChange={(e) => setCheckinDate(e.target.value)}
            />
            <label> Check-in</label>
            <Field
              name="checkoutDate"
              type="date"
              placeholder="Check-out"
              value={checkoutDate}
              onChange={(e) => setCheckoutDate(e.target.value)}
            />
            <label> Check-in</label>
            <Field
              name="numberOfGuests"
              value={numberOfGuests}
              onChange={(e) => setNumberOfGuests(e.target.value)}
            />
            <button className="button" type="submit">
              Selecionar quarto(s)
            </button>
            {/* <button type="submit">Submit</button>
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
            <label>Valor mínimo</label>
            <input
              id="minDailyRate"
              onChange={(e) => setMinDailyRate(e.target.value)}
            />
            <label>Valor máximo</label>
            <input
              id="maxDailyRate"
              onChange={(e) => setMaxDailyRate(e.target.value)}
            />
            <button className="button" type="submit">
              Selecionar quarto(s)
            </button> */}
          </Form>
        </Formik>
      </div>
    </div>
  );
}
