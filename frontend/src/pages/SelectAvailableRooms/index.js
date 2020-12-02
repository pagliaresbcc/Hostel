import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { AiFillPlusSquare, AiFillCheckSquare } from "react-icons/ai";

import "./styles.css";

import logoImg from "../../assets/images/logo.png";
import api from "../../services/api";

export default function SelectAvailableRooms() {
  const [rooms, setRoom] = useState([]);
  const token = sessionStorage.getItem("token");
  const checkinDate = sessionStorage.getItem("checkinDate");
  const checkoutDate = sessionStorage.getItem("checkoutDate");
  const numberOfGuests = sessionStorage.getItem("numberOfGuests");
  const minDailyRate = sessionStorage.getItem("minDailyRate");
  const maxDailyRate = sessionStorage.getItem("maxDailyRate");

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
      </header>

      <div className="rooms-header-label">
        <h1>Quartos Disponiveis</h1>
        <Link className="button" to="/payments/selectPayment">
            Selecionar forma de pagamento
        </Link>
      </div>
      <ul>
        {rooms.map((room, j) => (
          <li key={room.id}>
            <strong>QUARTO {room.number}:</strong>
            <p>{room.description}</p>

            <strong>DIMENSÃO:</strong>
            <p>{room.dimension} m²</p>

            <strong>LIMITE DE HÓSPEDES:</strong>
            <p>{room.maxNumberOfGuests} pessoas</p>

            <strong>VALOR DA DIÁRIA:</strong>
            <p>R$ {room.dailyRate.price},00</p>

            <button
              type="button"
              onClick={() => {
                handleSelectRoom(room.id);
              }}
            >
              {selectedItems.includes(room.id) ? (
                <AiFillCheckSquare size="28px" color="#999" />
              ) : (
                <AiFillPlusSquare size="28px" color="#999" />
              )}
            </button>
          </li>
        ))}
        {sessionStorage.setItem("rooms_ID", JSON.stringify(selectedItems))}
      </ul>
    </div>
  );
}
