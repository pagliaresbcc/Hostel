import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import Select from "react-select";

import api from "../../services/api";
import "./styles.css";

import logoImg from "../../assets/images/logo.png";

export default function NewReservation() {
  const [numberOfGuests, setNumberOfGuests] = useState("");
  const [checkinDate, setCheckinDate] = useState("");
  const [checkoutDate, setCheckoutDate] = useState("");
  const [number, setNumber] = useState("");

  const [type, setType] = useState("");

  const [rooms, setRooms] = useState([
    {
      description: "",
      number: 0,
      dimension: 0,
      maxNumberOfGuests: 0,
    },
  ]);

  const token = localStorage.getItem("token");

  const history = useHistory();

  async function handleRegister(e) {
    e.preventDefault();

    const room = {
      number,
    };

    const data = {
      checkinDate,
      checkoutDate,
      numberOfGuests,
      number,
      room,
    };

    try {
      await api.post("api/reservations", data, {
        headers: { Authorization: "Bearer " + token },
      });

      alert("Reserva cadastrada com sucesso!");

      history.push("/profile");
    } catch (err) {
      alert("Erro no cadastro, tente novamente");
    }
  }

  function showCheckPaymentOptions() {
    return (
      <div className="input-pagamento">
        <input
          placeholder="Check-in"
          value={checkinDate}
          onChange={(e) => setCheckinDate(e.target.value)}
        />
        <input
          placeholder="Check-in"
          value={checkinDate}
          onChange={(e) => setCheckinDate(e.target.value)}
        />
      </div>
    );
  }

  function showCashPaymentOptions() {
    return (
      <div className="input-pagamento">
        <input
          placeholder="Check-in"
          value={checkinDate}
          onChange={(e) => setCheckinDate(e.target.value)}
        />
        <input
          placeholder="Check-in"
          value={checkinDate}
          onChange={(e) => setCheckinDate(e.target.value)}
        />
      </div>
    );
  }

  function showCreditCardPaymentOptions() {
    return <h1>this.payment</h1>;
  }

  function handlePayment(option) {
    setType(option);
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
            placeholder="Check-in"
            value={checkinDate}
            onChange={(e) => setCheckinDate(e.target.value)}
          />

          <input
            placeholder="Check-out"
            value={checkoutDate}
            onChange={(e) => setCheckoutDate(e.target.value)}
          />

          <div className="input-pagamento">
            <Select
              placeholder="Selecione a forma de pagamento"
              value={type}
              onChange={handlePayment}
              options={[
                {
                  value: 1,
                  label: "Dinheiro",
                },
                {
                  value: 2,
                  label: "Cheque",
                },
                {
                  value: 3,
                  label: "Cartão de Crédito",
                },
              ]}
            />

            {type.value === 1 ? (
              <input
                placeholder="Chec222222222k-in"
                value={checkinDate}
                onChange={(e) => setCheckinDate(e.target.value)}
              />
            ) : type.value === 2 ? (
              <div>
                <input
                  placeholder="sdadsaasddsa-in"
                  value={checkinDate}
                  onChange={(e) => setCheckinDate(e.target.value)}
                />
                <input
                  placeholder="sdadsaasddsa-in"
                  value={checkinDate}
                  onChange={(e) => setCheckinDate(e.target.value)}
                />
              </div>
            ) : type.value === 3 ? (
              <div>
                <input
                  placeholder="sdadsaasddsa-in"
                  value={checkinDate}
                  onChange={(e) => setCheckinDate(e.target.value)}
                />
                <input
                  placeholder="sdadsaasddsa-in"
                  value={checkinDate}
                  onChange={(e) => setCheckinDate(e.target.value)}
                />
                <input
                  placeholder="sdadsaasddsa-in"
                  value={checkinDate}
                  onChange={(e) => setCheckinDate(e.target.value)}
                />
              </div>
            ) : 
              <h1></h1>
            }
          </div>
          <button className="button" type="submit">
            Cadastrar
          </button>
        </form>
      </div>
    </div>
  );
}
