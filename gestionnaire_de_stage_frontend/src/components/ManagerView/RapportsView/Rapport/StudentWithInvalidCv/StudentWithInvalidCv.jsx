import React, {useEffect, useState} from "react";
import {getStudentsWithInvalidCv} from "../../../../../services/user-service";
import {Table, TableHeader, TableRow} from "../../../../SharedComponents/Table/Table";


export default function StudentWithInvalidCv() {

    const [studentList, setStudentList] = useState([])

    useEffect(() => {
        getStudentsWithInvalidCv()
            .then(studentList => {
                setStudentList(studentList)
            })
            .catch(e => {
                setStudentList([])
                console.error(e);
            })
    }, [])

    if (studentList.length === 0) {
        return <div className={'bg-secondary d-flex py-3 align-items-center justify-content-center text-white'}>
            Tous les étudiants ont un CV valide
        </div>
    }

    return (
        <div className='container'>
            <Table className={"w-75 mx-auto"}>
                <TableHeader>
                    <th>#</th>
                    <th>Étudiant</th>
                    <th>Matricule</th>
                    <th>Adresse électronique</th>
                </TableHeader>
                {studentList.map((student, index) =>
                    <TableRow key={index}>
                        <th>{student.id}</th>
                        <td>{student.firstName} {student.lastName}</td>
                        <td>{student.matricule}</td>
                        <td>{student.email}</td>
                    </TableRow>
                )}
            </Table>
        </div>
    )
}
