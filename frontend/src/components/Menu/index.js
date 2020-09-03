import React, { Component } from 'react';

//import './styles.css';


class Card extends Component {
    constructor() {
        super();

        this.state = {
            showMenu: false,
        };

        this.showMenu = this.showMenu.bind(this);
        this.closeMenu = this.closeMenu.bind(this);
    }

    showMenu(event) {
        event.preventDefault();

        this.setState({ showMenu: true }, () => {
            document.addEventListener('click', this.closeMenu);
        });
    }

    closeMenu() {
        this.setState({ showMenu: false }, () => {
            document.removeEventListener('click', this.closeMenu);
        });
    }


    render() {
        return ( 
        <div className="dropdown" onClick = { this.showMenu }>
            <button className="dropbtn">
                <div className="container">
                <div className="bar1"></div>
                <div className="bar2"></div>
                <div className="bar3"></div>
                </div>
            </button>

            {   this.state.showMenu ?
                    ( <div className="dropdown-content">
                    <a href="#">Link 1</a>
                    <a href="#">Link 2</a>
                    <a href="#">Link 3</a>
                    </div>
                    ) :
                    (
                        null
                    )
            } 
            </div>

        );
    }
}

export default Card;