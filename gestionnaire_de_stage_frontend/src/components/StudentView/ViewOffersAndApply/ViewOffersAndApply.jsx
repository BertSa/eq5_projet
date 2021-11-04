import React, {useEffect, useState} from 'react';
import {getAllOffersByDepartment} from '../../../services/offer-service';
import ApplyOnOffer from "../ApplyOnOffer/ApplyOnOffer";
import {useAuth} from "../../../services/use-auth";

export default function ViewOffersAndApply() {//TODO: remove Offer after applied
    let auth = useAuth();
    const [offers, setOffers] = useState([])

    useEffect(() => {
        getAllOffersByDepartment(auth.user.department)
            .then(offers => {
                console.log(offers)

                setOffers(offers)
            })
            .catch(e => {
                setOffers([])
                console.error(e);
            });
    }, [auth.user.department]);


    return (
        <div className='container'>
            <h2 className="text-center">Offres de Stage</h2>
            <ul>
                {offers.map((offer, index) => <li key={index}><ApplyOnOffer offer={offer}/></li>)}
            </ul>
        </div>
    );
}