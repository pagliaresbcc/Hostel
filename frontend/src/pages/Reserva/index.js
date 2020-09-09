import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FiPower, FiTrash2 } from 'react-icons/fi';

import './styles.css';

import logoImg from '../../assets/logo.png';
import api from '../../services/api';

export default function Reserva(){
    const [reservas, setReserva] = useState([]);

    const token = localStorage.getItem('token');

    useEffect(() => {
        api.get('api/reservations', {
            headers: {'Authorization': 'Bearer '+token}
        }).then(response => {
            setReserva(response.data);
        })
    }, [token]);

    async function handleDeleteReservation( id){

        try {
            alert('Deletou');
            api.delete(`api/reservations/${id}`, {
                headers: {
                    'Authorization': 'Bearer '+token,
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET,PUT,POST,DELETE,PATCH,OPTIONS"
                }
            });
        } catch (err){
            alert('Erro ao deletar caso, tente novamente.');
        }
    }

    return(
        <div className="profile-container">
            <header>
                <img src={logoImg} alt="Logo" />
                <span>Bem vindos ao Hostel</span>

                <Link className="button" to="/new/reservas">Cadastrar nova reserva</Link>
                <button type="button">
                    <FiPower size={18} color="#E02041"/>
                </button>
            </header>

            <h1>Reservas Cadastradas</h1>

            <ul>
                {reservas.map(({id, rooms, checkinDate, checkoutDate, payments}, i)=> (
                    <li key={id}>
                    <strong>QUARTOS RESERVADOS PARA O CUSTOMER {id}:</strong>
                    {rooms.map((rooms, j) => <p key={j}>{rooms.number}</p>)}

                    <strong>CHECKIN DATE:</strong>
                    <p>{checkinDate}</p>

                    <strong>CHECKOUT DATE:</strong>
                    <p>{checkoutDate}</p>

                    <strong>VALOR PAGAMENTO:</strong>
                    <p>R$ {payments.amount}</p>

                    <button onClick={()=>handleDeleteReservation(id)} type="button">
                        <FiTrash2 size={20} color="#a8a8b3"/>
                    </button>
                    </li>
                ))}
                
            </ul>
        </div>
    );
}

