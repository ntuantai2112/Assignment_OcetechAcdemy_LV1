import React, { Component, Fragment } from "react";
import { connect } from "react-redux";
import { PropTypes } from "prop-types";
import { setUserData } from "../redux/actions/UserActions";
import jwtAuthService from "../services/jwtAuthService";
import localStorageService from "../services/localStorageService";
import firebaseAuthService from "../services/firebase/firebaseAuthService";
import history from "history.js";
import ConstantList from "../appConfig";
class Auth extends Component {
  state = {};
  
  constructor(props) {
    super(props);
    let userEntity = localStorageService.getItem("auth_user");
    let token = localStorageService.getItem("jwt_token");
    //let tokenData = localStorageService.getSessionItem("token_data");
    //console.log(tokenData);
    let expire_time= localStorageService.getItem("token_expire_time");
    let dateObj = new Date(expire_time);
    //alert('Auth:'+expire_time);
    if(token){
      jwtAuthService.setSession(token);
    }
    var isExpired = false;
    if(dateObj!=null){
      if(dateObj<Date.now()){
        isExpired=true;
      }
    }
    if(userEntity!=null && (isExpired==false)){
      this.props.setUserData(userEntity);
    }else {
      history.push(ConstantList.LOGIN_PAGE)
    }
    
    //this.checkJwtAuth();
    // this.checkFirebaseAuth();
  }

  checkJwtAuth = () => {
    jwtAuthService.loginWithToken().then(userEntity => {
      this.props.setUserData(userEntity);
    });
  };

  checkFirebaseAuth = () => {
    firebaseAuthService.checkAuthStatus(userEntity => {
      if (userEntity) {
        console.log(userEntity.uid);
        console.log(userEntity.email);
        console.log(userEntity.emailVerified);
        console.log(userEntity.getItem);
      } else {
        console.log("not logged in");
      }
    });
  };

  render() {
    const { children } = this.props;
    return <Fragment>{children}</Fragment>;
  }
}

const mapStateToProps = state => ({
  setUserData: PropTypes.func.isRequired,
  login: state.login
});

export default connect(
  mapStateToProps,
  { setUserData }
)(Auth);
