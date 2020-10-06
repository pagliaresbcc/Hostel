import React, { useState, useEffect } from "react";
import { Link, useHistory } from "react-router-dom";
import { AiFillPlusSquare, AiFillCheckSquare } from "react-icons/ai";

import "./styles.css";

import logoImg from "../../assets/images/logo.png";
import api from "../../services/api";

export default function SelectAvailableRooms() {
  const [rooms, setRoom] = useState([]);
  const token = localStorage.getItem("token");
  const checkinDate = localStorage.getItem("checkinDate");
  const checkoutDate = localStorage.getItem("checkoutDate");
  const numberOfGuests = localStorage.getItem("numberOfGuests");
  const minDailyRate = localStorage.getItem("minDailyRate");
  const maxDailyRate = localStorage.getItem("maxDailyRate");

  const [selectedItems, setSelectedItems] = useState([]);

  const history = useHistory();

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

  if (token === null) {
    history.push("/");
    return <div></div>;
  } else {
    return (
      <div className="profile-container">
        <header>
          <img src={logoImg} alt="Logo" />
          <span>Bem vindo ao Hostel</span>

          <Link className="button" to="/payments/selectPayment">
            Selecionar forma de pagamento
          </Link>
        </header>

        <h1>Quartos Disponiveis</h1>

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
          {localStorage.setItem("rooms_ID", JSON.stringify(selectedItems))}
        </ul>
      </div>
    );
  }
}
