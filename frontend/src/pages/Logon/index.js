import React, { useState } from 'react';
import { Link, useHistory } from 'react-router-dom';
import { FiLogIn } from 'react-icons/fi';

import './styles.css';

import api from '../../services/api';

import logoImg from '../../assets/images/logo.png';

export default function Logon(){
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const history = useHistory();

    async function handleLogin(e){
        e.preventDefault();

        const data = {
            email,
            password,
        }

        try {
            const response = await api.post('auth', data);

            sessionStorage.setItem('customer_ID', response.data.customer_ID);
            sessionStorage.setItem('token', response.data.token);
            sessionStorage.setItem('type', response.data.type);
            sessionStorage.setItem('role', response.data.role);

            history.push('/admin/profileAdmin');
        } catch (err) {
            alert('Falha no Login, tente novamente');
        }
    }

    return(
        <div className="logon-container">
            <section className="form">
                <img src={logoImg} alt="Logo" />

                <form onSubmit={handleLogin}>
                    <h1>Faça seu logon</h1>

                    <input type="email"
                        placeholder="Seu email" 
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                    />
                    <input type="password"
                        placeholder="Sua senha"
                        value={password}
                        onChange={e => setPassword(e.target.value)}    
                    />
                    <button className="button" type="submit" >Entrar</button>

                    <Link className="back-link" to="/register">
                        <FiLogIn size={16} color="#E02041"/>
                        Não tenho cadastro
                    </Link>
                </form>
            </section>

            
        </div>
    );
}