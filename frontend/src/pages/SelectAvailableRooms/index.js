import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { BsPlusSquare } from "react-icons/bs";

import "./styles.css";

import logoImg from "../../assets/images/logo.png";
import api from "../../services/api";

export default function Profile() {
  const [rooms, setRoom] = useState([]);
  const token = localStorage.getItem("token");
  const checkinDate = localStorage.getItem("checkinDate");
  const checkoutDate = localStorage.getItem("checkoutDate");
  const numberOfGuests = localStorage.getItem("numberOfGuests");
  const minDailyRate = localStorage.getItem("minDailyRate");
  const maxDailyRate = localStorage.getItem("maxDailyRate");

  const [selectedItems, setSelectedItems] = useState([]);

  useEffect(() => {
    api
      .get("api/rooms", {
        headers: { Authorization: "Bearer " + token },
        params: {
          checkinDate,
          checkoutDate,
          numberOfGuests,
          minDailyRate,
          maxDailyRate,
        },
      })
      .then((response) => {
        setRoom(response.data);
      });
  });

  function handleSelectRoom(id) {
    const alreadySelected = selectedItems.findIndex((rooms) => rooms === id);

    if (alreadySelected >= 0) {
      const filteredItems = selectedItems.filter((rooms) => rooms !== id);
      setSelectedItems(filteredItems);
    } else {
      setSelectedItems([...selectedItems, id]);
    }
  }

  return (
    <div className="profile-container">
      <header>
        <img src={logoImg} alt="Logo" />
        <span>Bem vindo ao Hostel</span>

        <Link className="button" to="/payment/selectPayment">
          Selecionar forma de pagamento
        </Link>
      </header>

      <h1>Quartos Disponiveis</h1>

      <ul>
        {rooms.map((room) => (
          <li key={room.id}>
            <strong>QUARTO {room.number}:</strong>
            <p>{room.description}</p>

            <strong>DIMENSÃO:</strong>
            <p>{room.dimension} m²</p>

            <strong>LIMITE DE HÓSPEDES:</strong>
            <p>{room.maxNumberOfGuests} pessoas</p>

            <strong>VALOR:</strong>
            <p>R$ {room.dailyRate.price},00</p>

            <button
              onClick={() => handleSelectRoom(room.id)}
              className={selectedItems.includes(room.id) ? "selected" : ""}
              type="button"
            >
              <BsPlusSquare className="selectButton" size={30} color="#a8a8b3"></BsPlusSquare>
            </button>
          </li>
        ))}
        {localStorage.setItem("rooms_ID", JSON.stringify(selectedItems))}
      </ul>
    </div>
  );
}
