class ButtonCard extends Component {
  constructor(props) {
    super(props);
    this.logOut = this.logOut.bind(this);

    this.state = {
      isCardView: false,
    };
  }
}