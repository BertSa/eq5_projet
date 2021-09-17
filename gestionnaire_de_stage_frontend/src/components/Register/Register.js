import './Register.css'
import React, {Component} from "react";

export class Register extends Component {
    state = {
        step: 1,
        email: '',
        password: '',
        last_name: '',
        first_name: '',
        phone: '',
        companyName: '',
        address: '',
        codePostal: '',
        city: '',
    }
    prevStep = () => {
        const {step} = this.state;
        if (step !== 1) {
            this.setState({step: step - 1});
        }
    }

    nextStep = () => {
        const {step} = this.state;
        this.setState({step: step + 1});
    }
    handleChange = input => e => {
        this.setState({[input]: e.target.value});
    }

    render() {
        const {step} = this.state;
        const {
            email,
            password,
            first_name,
            last_name,
            city,
            phone,
            companyName,
            address,
            codePostal
        } = this.state;
        const values = {email, password, first_name, last_name,phone}
        const valMoniteur = {companyName, city, address, codePostal}
        let show = null;
        switch (step) {
            case 1:
                show = <InformationGeneral nextStep={this.nextStep} handleChange={this.handleChange}
                                           values={values}/>
                break;
            case 2:
                show = <Moniteur prevStep={this.prevStep} nextStep={this.nextStep} handleChange={this.handleChange}
                                 values={valMoniteur}/>
                break;
            default:
                break;
        }
        return <div>
            <div>
                email:{email}<br/>
                lastname:{last_name}<br/>
                firstname:{first_name}<br/>
                password:{password}<br/>
                city:{city}<br/>
                companyName:{companyName}<br/>
                phone:{phone}<br/>
                address:{address}<br/>
                codePostal:{codePostal}<br/>
            </div>
            <div className="form-container">
                <form className="bg-dark px-3 py-4 rounded shadow-lg mt-5" id="contact_form">
                    <fieldset>

                        <legend>
                            <center><h2>Inscription</h2></center>
                        </legend>
                        <br/>
                        {show}
                    </fieldset>
                </form>
            </div>
        </div>;


    }
}

const InformationGeneral = ({nextStep, handleChange, values}) => {

    const Continue = e => {
        e.preventDefault();
        nextStep();
    }

    return (<div>
            <div className="form-group row">
                <div className="col-md-6">
                    <label>Prenom</label>
                    <div className="input-group">
                        <input name="first_name" placeholder="Prenom" className="form-control" type="text"
                               value={values.first_name} onChange={handleChange('first_name')}/>
                    </div>
                </div>
                <div className="col-md-6">
                    <label>Nom</label>
                    <div>
                        <div className="input-group">
                            <input name="last_name" placeholder="Nom" className="form-control" type="text"
                                   value={values.last_name} onChange={handleChange('last_name')}/>
                        </div>
                    </div>
                </div>
            </div>
            <div className="form-group">
                <label>E-Mail</label>
                <div className="input-group">
                    <input name="email" placeholder="Adresse E-mail" className="form-control" type="text"
                           value={values.email} onChange={handleChange("email")}/>
                </div>
            </div>
            <div className="form-group">
                <label>Téléphone</label>
                <div className="input-group">
                    <input name="contact_no" placeholder="000 000 000" className="form-control" type="text"
                           value={values.phone} onChange={handleChange('phone')}/>
                </div>
            </div>
            <div className="form-group text-center">
                <label/>
                <div>
                    <button className="btn btn-primary" type={"button"} onClick={Continue}>Suivant</button>
                </div>
            </div>
        </div>
    )
}
const Moniteur = ({prevStep, nextStep, handleChange, values}) => {

    const Previous = e => {
        e.preventDefault();
        prevStep();
    }
    const Continue = e => {
        e.preventDefault();
        nextStep();
    }

    return (<div>
            <div className="form-group row">
                <div className="col-md-6">
                    <label>Nom de la compagnie</label>
                    <div className="input-group">
                        <input name="companyName" placeholder="Nom de compagnie" className="form-control" type="text"
                               value={values.companyName} onChange={handleChange('companyName')}/>
                    </div>
                </div>
                <div className="col-md-6">
                    <label>Ville</label>
                    <div>
                        <div className="input-group">
                            <input name="city" placeholder="Ville" className="form-control" type="text"
                                   value={values.city} onChange={handleChange('city')}/>
                        </div>
                    </div>
                </div>
            </div>
            <div className="form-group">
                <label>Adresse de la compagnie</label>
                <div className="input-group">
                    <input name="address" placeholder="Rue, boulevard, avenue.." className="form-control" type="text"
                           value={values.address} onChange={handleChange("address")}/>
                </div>
            </div>
            <div className="form-group">
                <label>Code Postale</label>
                <div className="input-group">
                    <input name="codePostal" placeholder="XXX 123" className="form-control" type="text"
                           value={values.codePostal} onChange={handleChange('codePostal')}/>
                </div>
            </div>
            <div className="form-group text-center">
                <label/>
                <div>
                    <button className="btn btn-primary" type={"button"} onClick={Previous}>Precedent</button>
                    <button className="btn btn-primary" type={"button"} onClick={Continue}>Suivant</button>
                </div>
            </div>
        </div>
    )
}