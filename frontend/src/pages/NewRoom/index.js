import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import api from "../../services/api";
import "./styles.css";

import logoImg from "../../assets/images/logo.png";

export default function Profile() {
  const [number, setNumber] = useState("");
  const [description, setDescription] = useState("");
  const [dimension, setDimension] = useState("");
  const [maxNumberOfGuests, setMaxNumberOfGuests] = useState("");
  const [price, setPrice] = useState("");

  const history = useHistory();

  const token = sessionStorage.getItem("token");

  async function handleRegister(e) {
    e.preventDefault();

    const dailyRate = {
      price,
    };

    const data = {
      number,
      description,
      dimension,
      maxNumberOfGuests,
      dailyRate,
    };

    try {
      await api.post("api/rooms", data, {
        headers: { Authorization: "Bearer " + token },
      });

      alert("Cadastrado");

      history.push("/rooms");
    } catch (err) {
      alert("Erro no cadastro, tente novamente");
    }
  }

  return (
    <div className="novo-quarto-container">
      <div className="content">
        <section>
          <img src={logoImg} alt="Logo" />

          <h1>Cadastrar novo quarto</h1>
          <p>Adicione mais uma acomadação especial para os clientes</p>

          <Link className="back-link" to="/profile">
            <FiArrowLeft size={16} color="#E02041" />
            Voltar
          </Link>
        </section>

        <form onSubmit={handleRegister}>
          <input
            placeholder="Número do quarto"
            value={number}
            onChange={(e) => setNumber(e.target.value)}
          />

          <textarea
            placeholder="Descrição"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
          />

          <input
            placeholder="Dimensão (em m²)"
            value={dimension}
            onChange={(e) => setDimension(e.target.value)}
          />

          <input
            placeholder="Número máximo de hóspedes"
            value={maxNumberOfGuests}
            onChange={(e) => setMaxNumberOfGuests(e.target.value)}
          />

          <input
            placeholder="Valor da diária (em R$)"
            value={price}
            onChange={(e) => setPrice(e.target.value)}
          />

          <button className="button" type="submit">
            Cadastrar
          </button>
        </form>
      </div>
    </div>
  );
  
}
