import React, { useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import Select from "react-select";

import api from "../../services/api";
import "./styles.css";

import logoImg from "../../assets/images/logo.png";

export default function NewReservation() {
  const [amount, setAmount] = useState("");
  const [amountTendered, setAmountTendered] = useState("");
  const [bankId, setBankId] = useState("");
  const [bankName, setBankName] = useState("");
  const [branchNumber, setBranchNumber] = useState("");
  const [issuer, setIssuer] = useState("");
  const [cardNumber, setCardNumber] = useState("");
  const [nameOnCard, setNameOnCard] = useState("");
  const [expirationDate, setExpirationDate] = useState("");
  const [securityCode, setSecurityCode] = useState("");

  const [type, setType] = useState("");

  const token = localStorage.getItem("token");

  const history = useHistory();

  const customer_ID = localStorage.getItem("customer_ID");
  const checkinDate = localStorage.getItem("checkinDate");
  const checkoutDate = localStorage.getItem("checkoutDate");
  const numberOfGuests = localStorage.getItem("numberOfGuests");
  const rooms_ID = JSON.parse(localStorage.getItem("rooms_ID"));

  var data = {};

  const customStyles = {
    option: (provided, state) => ({
      ...provided,
    }),
    control: (provided) => ({
      ...provided,
      marginBottom: "20px",
      width: "100%",
      height: "60px",
      color: "#333",
      border: "1px solid #dcdce6",
      font: "400 16px Roboto, sans-serif",
    }),
  };

  async function handleRegister(e) {
    e.preventDefault();

    try {
      var payment = {};

      if (type.value === 1) {
        payment = {
          type: "cash",
          amount,
          amountTendered,
        };
      } else if (type.value === 2) {
        payment = {
          type: "check",
          amount,
          bankId,
          bankName,
          branchNumber,
        };
      } else if (type.value === 3) {
        payment = {
          type: "creditCard",
          amount,
          issuer,
          cardNumber,
          nameOnCard,
          expirationDate,
          securityCode,
        };
      }

      data = {
        customer_ID,
        checkinDate,
        checkoutDate,
        numberOfGuests,
        payment,
        rooms_ID,
      };

      await api.post("api/reservations", data, {
        headers: { Authorization: "Bearer " + token },
      });

      history.push("/profile");
    } catch (err) {
      console.log(data);
      alert("Erro nas informações, tente novamente");
    }
  }

  function handlePayment(option) {
    var total = 0;

    var date1 = new Date(checkinDate);
    var date2 = new Date(checkoutDate);
    const diffTime = Math.abs(date2 - date1);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;

    setType(option);

    rooms_ID.forEach((roomId) => {
      api
        .get(`api/rooms/${roomId}`, {
          headers: { Authorization: "Bearer " + token },
        })
        .then((response) => {
          total += response.data.dailyRate.price * diffDays;
          setAmount(total);
          setAmountTendered(total * 0.9);
        });
    });
  }

  if (token === null) {
    history.push("/");
    return <div></div>;
  } else {
    return (
      <div className="nova-reserva-container">
        <div className="content">
          <section>
            <img src={logoImg} alt="Logo" />

            <h1>Selecione a forma de pagamento</h1>

            <Link className="back-link" to="/room/selectAvailableRooms">
              <FiArrowLeft size={16} color="#E02041" />
              Voltar para quartos disponíveis
            </Link>
          </section>
          <form onSubmit={handleRegister}>
            <div className="input-pagamento">
              <Select
                id="payment"
                styles={customStyles}
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
                <div className="input-valor">
                  <label>Valor à vista com 10% de desconto: </label>
                  <p>R$ {amountTendered},00</p>
                </div>
              ) : type.value === 2 ? (
                <div>
                  <label id="label-valor">Valor: </label>
                  <p>R$ {amount},00</p>
                  <input
                    placeholder="Agência"
                    value={bankId}
                    onChange={(e) => setBankId(e.target.value)}
                  />
                  <input
                    placeholder="Nome do Banco"
                    value={bankName}
                    onChange={(e) => setBankName(e.target.value)}
                  />
                  <input
                    placeholder="Número da conta (Com o dígito)"
                    value={branchNumber}
                    onChange={(e) => setBranchNumber(e.target.value)}
                  />
                </div>
              ) : type.value === 3 ? (
                <div>
                  <label id="label-valor">Valor: </label>
                  <p>R$ {amount},00</p>
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
              ) : (
                <div></div>
              )}
            </div>

            <button className="button" type="submit">
              Cadastrar reserva
            </button>
          </form>
        </div>
      </div>
    );
  }
}
