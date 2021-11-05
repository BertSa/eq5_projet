import ContratSignature from "../ContractSignature/ContratSignature";
import {useAuth} from "../../services/use-auth";
import {useEffect, useState} from "react";
import {getContractForStudent} from '../../services/contrat-service';
import {UserType} from "../../enums/UserTypes";

export function ViewContract(){
    const auth = useAuth();
    const [contract, setContract] = useState(null);

    useEffect(() => {
        getContractForStudent(auth.user.id).then(contract => setContract(contract));
    }, []);


    return <>
        <ContratSignature contract={contract} userType={UserType.STUDENT[0]} userId={auth.user.id} />
    </>
}
