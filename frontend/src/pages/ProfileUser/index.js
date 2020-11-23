import React from "react";
import { Link } from "react-router-dom";
import { FiPower } from "react-icons/fi";

import logoImg from "../../assets/images/logo.png";

import "./styles.css";

export default function ProfileUser() {

  return (
    <div className="profile-container">
      <header>
        <img src={logoImg} alt="Logo" />
        <span>Olá, Usuário, bem-vindo ao Hostel!</span>

        <Link className="back-link" to="/">
            <FiPower size={18} color="#E02041" />
        </Link>
      </header>

      <div className="profile-grid">
        <Link className="button" to="/reservations">
          Gerenciar reservas
        </Link>
        <Link className="button" to="/user/updateCustomer">
          Atualizar perfil
        </Link>
      </div>
    </div>
  );
  
}
