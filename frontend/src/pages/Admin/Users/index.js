import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiPower, FiTrash2, FiEdit3, FiArrowLeft } from "react-icons/fi";

import "./styles.css";

import logoImg from "../../../assets/images/logo.png";
import api from "../../../services/api";

export default function Guest() {
  const [guest, setGuest] = useState([]);

  const history = useHistory();

  const token = sessionStorage.getItem("token");

  useEffect(() => {
    api
      .get("api/guests", {
        headers: { Authorization: "Bearer " + token },
      })
      .then((response) => {
        setGuest(response.data);
      });
  }, [token]);

  function handleUpdateGuest(id) {
    sessionStorage.setItem("guest_id", id);

    history.push("/admin/guests/update-guest");
  }

  async function handleDeleteGuest(id) {
    if (window.confirm("Tem certeza que deseja deletar este usuário?")) {
      try {
        api.delete(`api/guests/${id}`, {
          headers: {
            Authorization: "Bearer " + token,
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS",
          },
        });
        alert("Usuário deletado com sucesso!");
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
        <span>Bem vindo ao Hostel</span>

        <Link className="button" to="/admin/guests/new-guest">
          Cadastrar novo usuário
        </Link>
        <button type="button">
          <FiPower size={18} color="#E02041" />
        </button>
      </header>
      <h1>Usuários cadastrados</h1>
      <ul>
        {guest.map(({ id, title, name, lastName, email, address }, i) => (
          <li key={id}>
            <strong>
              {title} {name} {lastName}
            </strong>
            <p>{email}</p>

            <strong>Endereço:</strong>
            <p>Rua: {address.addressName}</p>
            <p>Cep: {address.zipCode}</p>
            <p>Cidade: {address.city}</p>
            <p>Estado: {address.state}</p>
            <p>Pais: {address.country}</p>

            <button
              className="deleteButton"
              onClick={() => handleDeleteGuest(id)}
              type="button"
            >
              <FiTrash2 size={20} color="#a8a8b3" />
            </button>

            <button
              className="editButton"
              onClick={() => handleUpdateGuest(id)}
              type="button"
            >
              <FiEdit3 size={20} color="#a8a8b3" />
            </button>
          </li>
        ))}
      </ul>
      <Link className="back-link" to="/admin/profile">
        <FiArrowLeft size={16} color="#E02041" />
        Voltar
      </Link>
    </div>
  );
}
