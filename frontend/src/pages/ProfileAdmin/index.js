import React from "react";
import { Link, useHistory } from "react-router-dom";
import { FiPower } from "react-icons/fi";

import logoImg from "../../assets/images/logo.png";

import "./styles.css";

export default function ProfileAdmin() {
  const history = useHistory();

  const token = sessionStorage.getItem("token");

  if (token === null) {
    history.push("/");
    return <div></div>;
  } else {
    return (
      <div className="profile-container">
        <header>
          <img src={logoImg} alt="Logo" />
          <span>Olá, Administrador, bem-vindo ao Hostel!</span>

          <button type="button">
            <FiPower size={18} color="#E02041" />
          </button>
        </header>

        <div className="profileAdmin-grid">
          <Link className="button" to="/reservations">
            Gerenciar reservas
          </Link>
          <Link className="button" to="/rooms">
            Gerenciar quartos
          </Link>
          <Link className="button" to="">
            Gerenciar hóspedes
          </Link>
        </div>
      </div>
    );
  }
}
