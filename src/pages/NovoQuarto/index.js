import React from 'react';
import { Link } from 'react-router-dom';
import { FiArrowLeft } from 'react-icons/fi';

import './styles.css';

import logoImg from '../../assets/logo.png';

export default function Profile(){
    return(
        <div className="novo-quarto-container">
            <div className="content">
                <section>
                    <img src={logoImg} alt="Logo" />

                    <h1>Cadastrar novo quarto</h1>
                    <p>Adicione mais uma acomadação especial para nossos clientes</p>

                    <Link className="back-link" to="/profile">
                        <FiArrowLeft size={16} color="#E02041"/>
                        Voltar
                    </Link>
                </section>

                <form>
                    <input placeholder="Número do Quarto" />
                    <textarea placeholder="Descrição"/>
                    <input placeholder="Dimensão" />
                    

                    <button className="button" type="submit">Cadastrar</button>
                    
                </form>
            </div>
        </div>
    );
}