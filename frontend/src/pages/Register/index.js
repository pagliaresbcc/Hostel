import React, { useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi';

import api from '../../services/api';
import './styles.css';

import logoImg from '../../assets/images/logo.png';

export default function Register(){
    const [title, setTitle] = useState('');
    const [name, setName] = useState('');
    const [lastName, setLastName] = useState('');
    const [birthday, setBirthday] = useState('');
    const [addressName, setAddressName] = useState('');
    const [zipCode, setZipCode] = useState('');
    const [city, setCity] = useState('');
    const [state, setState] = useState('');
    const [country, setCountry] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const history = useHistory();

    async function handleRegister(e){
        e.preventDefault();

        const address = {
            addressName,
            zipCode,
            city,
            state,
            country,
        };

        const data = {
            title,
            name,
            lastName,
            birthday,
            address,
            email,
            password,
        };

        try{
            await api.post('api/customers', data);

            alert("Cadastrado");

            history.push('/');
        }catch (err){
            alert("Erro no cadastro, tente novamente");
        }
        
        
    }

    return(
        <div className="register-container">
            <div className="content">
                <section>
                    <img src={logoImg} alt="Logo" />

                    <h1>Cadastro</h1>
                    <p>Faça seu cadastro, entre na plataforma e faça já sua reserva.</p>

                    <Link className="back-link" to="/">
                        <FiArrowLeft size={16} color="#E02041"/>
                        Não tenho cadastro
                    </Link>
                </section>

                <form onSubmit={handleRegister}>

                    <input 
                        placeholder="Título"
                        value={title}
                        onChange={e => setTitle(e.target.value)} 
                    />

                    <div className="input-group">
                        
                        <input 
                            placeholder="Nome"
                            value={name}
                            onChange={e => setName(e.target.value)} 
                         />

                        <input 
                            placeholder="Sobrenome"
                            value={lastName}
                            onChange={e => setLastName(e.target.value)} 
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
                            onChange={e => setAddressName(e.target.value)} 
                         />
                        
                        <div className="input-group">
                            
                            <input 
                                placeholder="Cidade" 
                                value={city}
                                onChange={e => setCity(e.target.value)} 
                            />
                            
                            <input 
                                placeholder="Estado" 
                                value={state}
                                onChange={e => setState(e.target.value)} 
                            />

                        </div>
                        
                        <input 
                            placeholder="País"
                            value={country}
                            onChange={e => setCountry(e.target.value)}  
                        />

                        <input 
                            placeholder="CEP"
                            value={zipCode}
                            onChange={e => setZipCode(e.target.value)}  
                        />

                    </div>
                    
                    <input 
                        type="email" placeholder="E-mail"
                        value={email}
                        onChange={e => setEmail(e.target.value)} 
                    />
                    
                    <input type="password"
                        placeholder="Senha"
                        value={password}
                        onChange={e => setPassword(e.target.value)} 
                    />

                    <button className="button" type="submit">Cadastrar</button>
                    
                </form>
            </div>
        </div>
    );
}