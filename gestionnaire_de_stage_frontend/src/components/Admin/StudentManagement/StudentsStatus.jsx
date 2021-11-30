import React, {useEffect, useState} from "react";
import {getAllStudents} from "../../../services/user-service";
import {Table, TableHeader, TableRow} from "../../SharedComponents/Table/Table";
import {useAuth} from "../../../hooks/use-auth";
import {getStudentApplicationsOffer} from "../../../services/offerAppService";
import {useHistory} from "react-router-dom";
import MessageNothingToShow from "../../SharedComponents/MessageNothingToShow/MessageNothingToShow";
import {BtnBack} from "../../SharedComponents/BtnBack";
import {CgDetailsMore} from "react-icons/all";

export default function StudentsStatus() {

    const [studentList, setStudentList] = useState([])
    const [offerList, setOfferList] = useState([])
    const auth = useAuth()
    const history = useHistory()

    useEffect(() => {
        getAllStudents()
            .then(studentList => {
                setStudentList(studentList)
                studentList.forEach(student => {
                    getStudentApplicationsOffer(student.id)
                        .then(offerList => {
                            setOfferList(prev => [...prev, offerList || []])
                        })
                        .catch(e => {
                            setOfferList([])
                            console.error(e);
                        })
                })
            })
            .catch(e => {
                setStudentList([])
                console.error(e);
            })
    }, [auth.user.id]);

    if (studentList.length === 0 || offerList.length === 0)
        return <MessageNothingToShow message="Aucun étudiant n'a appliqué"/>

    return (
        <>
            <Table className={"w-75 mx-auto"}>
                <TableHeader>
                    <th>#</th>
                    <th>Étudiant</th>
                    <th>Nombre d'application</th>
                    <th><CgDetailsMore title={"Détails"} size={27} color={"black"}/></th>
                </TableHeader>
                {studentList.map((student, index) => {
                        if (!offerList[index] || offerList[index].length === 0) return null;
                        else return <TableRow key={index}>
                            <td>{student.id}</td>
                            <td>{student.firstName} {student.lastName}</td>
                            <td>{offerList[index].length}</td>
                            <td>
                                <button className="btn btn-primary"
                                        onClick={() => history.push({
                                            pathname: "offer",
                                            state: {student: student}
                                        })}>
                                    Voir
                                </button>
                            </td>
                        </TableRow>
                    }
                )}
            </Table>
            <BtnBack/>
        </>
    )
}