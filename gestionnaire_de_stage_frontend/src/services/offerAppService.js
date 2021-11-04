import {methods, requestInit, urlBackend} from "./serviceUtils";
import {swalErr} from "../utility";
import OfferApp from "../models/OfferApp";
import Swal from "sweetalert2";

class OfferAppService {

    url = `${urlBackend}/applications`;

    async apply(offerApp) {
        if (!this._isApplicationValid(offerApp))
            return;

        const response = await fetch(`${this.url}/apply`, requestInit(methods.POST, offerApp));

        return await response.then(res => res.json()).then(value => {
            if (value.message) {
                const valid = response.status === 201;
                Swal.fire({title: value.message, icon: valid ? 'success' : 'error'});
            }
        });
    }

    async getAllApplicants(email) {
        return await fetch(`${this.url}/applicants/${email}`, requestInit(methods.GET))
        .then(res => {
            if(res.status === 400) {
                res.json().then(err => swalErr().fire({text: err.message}))
                return Promise.any([]);
            }else{
                return res.json();
            }
        });
    }

    _isApplicationValid(offerApp) {
        return offerApp instanceof OfferApp &&
            offerApp.idOffer &&
            offerApp.idStudent;
    }
}

const offerAppService = new OfferAppService();
export default offerAppService;
