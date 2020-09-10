import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import Select from "react-select";

import api from "../../services/api";
import "./styles.css";

import logoImg from "../../assets/images/logo.png";

export default function NewReservation() {
  const [checkinDate, setCheckinDate] = useState("");
  const [checkoutDate, setCheckoutDate] = useState("");
  const [numberOfGuests, setNumberOfGuests] = useState("");
  const [minDailyRate, setMinDailyRate] = useState("");
  const [maxDailyRate, setMaxDailyRate] = useState("");
  
  const [amount, setAmount] = useState("");
  const [amountTendered, setAmountTendered] = useState("");
  const [bankId, setBankId] = useState("");
  const [branchName, setBranchName] = useState("");
  const [branchNumber, setBranchNumber] = useState("");
  const [issuer, setIssuer] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [nameOnCard, setNameOnCard] = useState("");
  const [expirationDate, setExpirationDate] = useState("");
  const [securityCode, setSecurityCode] = useState("");

  const [type, setType] = useState("");
  const [date, setDate] = useState ("");
  
  const [number, setNumber] = useState("");
  const [rooms, setRooms] = useState([
    {
      description: "",
      number: 0,
      dimension: 0,
      maxNumberOfGuests: 0,
    },
  ]);

  const[customerID, setCustomerID] = useState(""); 

  const token = localStorage.getItem("token");

  const history = useHistory();

  const customStyles = {
    option: (provided, state) => ({
      ...provided,
    }),
    control: (provided) => ({
      ...provided,
      marginTop: "2px",
      width: "100%",
      height: "60px",
      color: "#333",
      border: "1px solid #dcdce6",
      padding: "0 20px",
      font: "400 16px Roboto, sans-serif",
    })
  }

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
          <label for="check-out">Check-in</label>
          <input
            required="true"
            id="check-out"
            type="date"
            placeholder="Check-out"
            value={checkoutDate}
            onChange={(e) => setCheckoutDate(e.target.value)}
          />
          <label for="check-in">Check-out</label>
          <input
            required="true"
            id="check-in"
            type="date"
            placeholder="Check-in"
            value={checkinDate}
            onChange={(e) => setCheckinDate(e.target.value)}
          />
          <label for="numberOfGuests">Número de hóspedes</label>
          <input
            required="true"
            id="numberOfGuests"
            onChange={(e) => setNumberOfGuests(e.target.value)}
          />
          <label for="minDailyRate">Valor mínimo</label>
          <input
            id="minDailyRate"
            onChange={(e) => setMinDailyRate(e.target.value)}
          />
          <label for="maxDailyRate">Valor máximo</label>
          <input
            id="maxDailyRate"
            onChange={(e) => setMaxDailyRate(e.target.value)}
          />
          <label for="payment">Pagamento</label>
          <div className="input-pagamento">
            <Select
              id="payment"
              styles = { customStyles }
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
              <div>
                <input
                  placeholder="Valor"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                />
                <input
                  placeholder="Valor com desconto"
                  value={amountTendered}
                  onChange={(e) => setAmountTendered(e.target.value)}
                />
              </div>
            ) : type.value === 2 ? (
              <div>
                <input
                  placeholder="Valor"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                />
                <input
                  placeholder="Agência"
                  value={bankId}
                  onChange={(e) => setBankId(e.target.value)}
                />
                <input
                  placeholder="Nome do Banco"
                  value={branchName}
                  onChange={(e) => setBranchName(e.target.value)}
                />
                <input
                  placeholder="Número da conta (Com o dígito)"
                  value={branchNumber}
                  onChange={(e) => setBranchNumber(e.target.value)}
                />
              </div>
            ) : type.value === 3 ? (
              <div>
                <input
                  placeholder="Valor"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                />
                <input
                  placeholder="Emissor"
                  value={issuer}
                  onChange={(e) => setIssuer(e.target.value)}
                />
                <input
                  placeholder="Número do cartão"
                  value={cardNumber}
                  onChange={(e) => setCardNumber(e.target.value)}
                />
                <input
                  placeholder="Nome no cartão"
                  value={nameOnCard}
                  onChange={(e) => setNameOnCard(e.target.value)}
                />
                <input
                  placeholder="Validade"
                  value={expirationDate}
                  onChange={(e) => setExpirationDate(e.target.value)}
                />
                <input
                  placeholder="Código de segurança"
                  value={securityCode}
                  onChange={(e) => setSecurityCode(e.target.value)}
                />
              </div>
            ) : 
              <h1></h1>
            }
            <label for="rooms-id">Rooms_ID</label>
          </div>
          <button className="button" type="submit">
            Cadastrar
          </button>
        </form>
      </div>
    </div>
  );
}
