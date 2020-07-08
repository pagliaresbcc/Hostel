import React from 'react';
import { Link } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi';

import './styles.css';

import logoImg from '../../assets/logo.png';

export default function Profile(){
    return(
        <div className="nova-reserva-container">
            <div className="content">
                <section>
                    <img src={logoImg} alt="Logo" />

                    <h1>Cadastrar nova reserva</h1>
                    <p>Agende sua nova reserva para a data mais próxima</p>

                    <Link className="back-link" to="/">
                        <FiArrowLeft size={16} color="#E02041"/>
                        Não tenho cadastro
                    </Link>
                </section>

                <form>
                    <input placeholder="Check-in" />
                    <input placeholder="Check-out" />
                    <div className="input-pagamento">
                         <input placeholder="Forma de Pagamento" />
                        <input placeholder="Valor" />
                    </div>

                    <button className="button" type="submit">Cadastrar</button>
                    
                </form>
            </div>
        </div>
    );
}