import React, { useEffect, useState } from "react";
import { Link, useHistory } from "react-router-dom";
import { FiArrowLeft } from "react-icons/fi";

import logoImg from "../../assets/images/logo.png";
import api from "../../services/api";

export default function UpdateUser() {

  const history = useHistory();

  const [title, setTitle] = useState();

  const [name, setName] = useState();

  const [lastName, setLastName] = useState();

  const [birthday, setBirthday] = useState();

  const [addressName, setAddressName] = useState();

  const [zipCode, setZipCode] = useState();

  const [city, setCity] = useState();

  const [state, setState] = useState();

  const [country, setCountry] = useState();

  const guest_ID = sessionStorage.getItem("guest_ID");

  const token = sessionStorage.getItem("token");

  useEffect(() => {
    api
      .get(`api/guests/${guest_ID}`, {
        headers: { Authorization: "Bearer " + token },
      })
      .then((response) => {
        setTitle(response.data.title);
        setName(response.data.name);
        setLastName(response.data.lastName);
        setBirthday(response.data.birthday);
        setAddressName(response.data.address.addressName);
        setZipCode(response.data.address.zipCode);
        setCity(response.data.address.city);
        setState(response.data.address.state);
        setCountry(response.data.address.country);
      });
  }, []);

  async function handleUpdate(e) {
    e.preventDefault();

    var address = {
      addressName,
      zipCode,
      city,
      state,
      country
    }

    var data = {
      title,
      name,
      lastName,
      birthday,
      address,
    };

    await api.put(`api/guests/${guest_ID}`, data, {
      headers: { Authorization: "Bearer " + token },
    });

    history.push('/guest/profile');
  }

  return (
    <div className="register-container">
      <div className="content">
        <section>
          <img src={logoImg} alt="Logo" />

          <h1>Atualizar Cadastro</h1>

          <Link className="back-link" to="/guest/profile">
            <FiArrowLeft size={16} color="#E02041" />
            Voltar
          </Link>
        </section>

        <form onSubmit={handleUpdate}>
          <input
            required="true"
            placeholder="Título"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />

          <div className="input-group">
            <input
              required="true"
              placeholder="Nome"
              value={name}
              onChange={(e) => setName(e.target.value)}
            />

            <input
              required="true"
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
              required="true"
              placeholder="Endereço"
              value={addressName}
              onChange={(e) => setAddressName(e.target.value)}
            />

            <div className="input-group">
              <input
                required="true"
                placeholder="Cidade"
                value={city}
                onChange={(e) => setCity(e.target.value)}
              />

              <input
                required="true"
                placeholder="Estado"
                value={state}
                onChange={(e) => setState(e.target.value)}
              />
            </div>

            <input
              required="true"
              placeholder="País"
              value={country}
              onChange={(e) => setCountry(e.target.value)}
            />

            <input
              required="true"
              placeholder="CEP"
              value={zipCode}
              onChange={(e) => setZipCode(e.target.value)}
            />
          </div>

          <button className="button" type="submit">
            Atualizar
          </button>
        </form>
      </div>
    </div>
  );
}
