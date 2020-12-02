import React, { useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import logoImg from "../../assets/images/logo.png";


export default function UpdateCustomer() {
  const history = useHistory();

  const x = sessionStorage.getItem("token");
  console.log(x);
  useEffect(() => {
    history.go(0);
  }, [x]);

  const [title, setTitle] = useState(
    sessionStorage.getItem("title")
  );

  const [name, setName] = useState(
    sessionStorage.getItem("name")
  );
  
  const [lastName, setLastName] = useState(
    sessionStorage.getItem("lastName")
  );
  
  const [birthday, setBirthday] = useState(
    sessionStorage.getItem("birthday")
  );
  
  const [addressName, setAddressName] = useState(
    sessionStorage.getItem("addressName")
  );
  
  const [zipCode, setZipCode] = useState(
    sessionStorage.getItem("zipCode")
  );
  
  const [city, setCity] = useState(
    sessionStorage.getItem("city")
  );

  const [state, setState] = useState(
    sessionStorage.getItem("state")
  );

  const [country, setCountry] = useState(
    sessionStorage.getItem("country")
  );

  const [email, setEmail] = useState(
    sessionStorage.getItem("email")
  );

  function handleUpdate(e) {
    e.preventDefault();

    
  }

  return (
    <div className="register-container">
      <div className="content">
        <section>
          <img src={logoImg} alt="Logo" />

          <h1>Atualizar Cadastro</h1>

          <Link className="back-link" to="/admin/customers">
            <FiArrowLeft size={16} color="#E02041" />
            Voltar
          </Link>
        </section>

        <form onSubmit={handleUpdate}>
          <input
            placeholder="Título"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />

          <div className="input-group">
            <input
              placeholder="Nome"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <input
              placeholder="Sobrenome"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
          </div>

          <label>Data de nascimento</label>
          <input
            required="true"
            id="birthday"
            type="date"
            placeholder="Data de nascimento"
            value={birthday}
            onChange={(e) => setBirthday(e.target.value)}
          />

          <div className="input-endereco">
            <input
              placeholder="Endereço"
              value={addressName}
              onChange={(e) => setAddressName(e.target.value)}
            />

            <div className="input-group">
              <input
                placeholder="Cidade"
                value={city}
                onChange={(e) => setCity(e.target.value)}
              />

              <input
                placeholder="Estado"
                value={state}
                onChange={(e) => setState(e.target.value)}
              />
            </div>

            <input
              placeholder="País"
              value={country}
              onChange={(e) => setCountry(e.target.value)}
            />

            <input
              placeholder="CEP"
              value={zipCode}
              onChange={(e) => setZipCode(e.target.value)}
            />
          </div>

          <input
            type="email"
            placeholder="E-mail"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />

          <button className="button" type="submit">
            Atualizar
          </button>
        </form>
      </div>
    </div>
  );
}
