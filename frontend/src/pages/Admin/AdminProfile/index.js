import React from "react";
import { Link } from "react-router-dom";
import { FiPower } from "react-icons/fi";

import logoImg from "../../../assets/images/logo.png";

import "./styles.css";

export default function AdminProfile() {
  return (
    <div className="profile-container">
      <header>
        <img src={logoImg} alt="Logo" />
        <span>Olá Administrador, bem-vindo ao Hostel!</span>

        <button
          type="button"
          onClick={() => {
            sessionStorage.clear();
            window.location.reload();
          }}
          style={{ marginLeft: "60px" }}
        >
          <FiPower size={18} color="#E02041" />
        </button>
      </header>

      <div className="profile-grid">
        <Link className="button" to="/admin/reservations">
          Gerenciar reservas
        </Link>
        <Link className="button" to="/admin/rooms">
          Gerenciar quartos
        </Link>
        <Link className="button" to="/admin/guests">
          Gerenciar hóspedes
        </Link>
      </div>
    </div>
  );
}
