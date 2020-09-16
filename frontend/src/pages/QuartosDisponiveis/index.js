import React, { useState, useEffect } from 'react';
import { Link, useHistory } from "react-router-dom";
import { BsPlusSquare } from 'react-icons/bs';

import './styles.css';

import logoImg from '../../assets/images/logo.png';
import api from '../../services/api';

export default function Profile(){
    const [rooms, setRoom] = useState([]);
    const token = localStorage.getItem('token');
    const checkinDate = localStorage.getItem('checkinDate');
    const checkoutDate = localStorage.getItem('checkoutDate');
    const numberOfGuests = localStorage.getItem('numberOfGuests');
    const minDailyRate = localStorage.getItem('minDailyRate');
    const maxDailyRate = localStorage.getItem('maxDailyRate');

    const [selectedItems, setSelectedItems] = useState([]);

    const history = useHistory();

    useEffect(() => {
        api.get("api/rooms", {
            headers: { Authorization: "Bearer " + token },
            params: {
              checkinDate,
              checkoutDate,
              numberOfGuests,
              minDailyRate,
              maxDailyRate,
            }
          }).then(response => {
            setRoom(response.data);
        })
    }, [token]);


    function handleSelectRoom( id){
        const alreadySelected = selectedItems.findIndex((rooms) => rooms === id);
    
        if (alreadySelected >= 0) {
          const filteredItems = selectedItems.filter((rooms) => rooms !== id);
          setSelectedItems(filteredItems);
        } else {
          setSelectedItems([...selectedItems, id]);
        }

        localStorage.setItem('rooms_ID', JSON.stringify(selectedItems));
    }

    return(
        <div className="profile-container">
            <header>
                <img src={logoImg} alt="Logo" />
                <span>Bem vindos ao Hostel</span>

                <Link className="button" to="/pagamento">
                    Continuar
                </Link>
            </header>

            <h1>Quartos Disponiveis</h1>

            <ul>
                {rooms.map(room => (
                    <li key={room.id}>
                    <strong>QUARTO {room.number}:</strong>
                    <p>{room.description}</p>

                    <strong>DIMENSÃO:</strong>
                    <p>{room.dimension} m²</p>

                    <strong>LIMITE DE HÓSPEDES:</strong>
                    <p>{room.maxNumberOfGuests} pessoas</p>

                    <strong>VALOR:</strong>
                    <p>R$ {room.dailyRate.price}</p>

                    <button onClick={()=>handleSelectRoom(room.id)} className={selectedItems.includes(room.id) ? "selected" : ""} type="button">
                        <BsPlusSquare size={20} color="#a8a8b3"></BsPlusSquare>
                    </button>
                    {console.log(selectedItems)}
                    </li>

                    
                ))}
                
            </ul>
        </div>
    );
}