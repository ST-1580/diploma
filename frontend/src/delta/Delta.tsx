import "./Delta.css";
import axios from "axios";
import React, { useState, useEffect } from "react";
import { BACKEND_URL } from "../Utils";

type DeltaEntity = {
    id: number,
    name: string
}

const URL: string = BACKEND_URL + 'external/v1/delta';

function Delta() {
    const [activeDeltaEntites, setActiveDeltaEntites] = useState<DeltaEntity[]>([]);
    const [editEntityId, setEditEntityId] = useState<number | null>(null);

    useEffect(() => {
        axios.get(URL + '/entities/active')
            .then(response => {
                const newActiveDeltaEntities: DeltaEntity[] = response.data.map(
                    (e: any) => {
                        const parsedEntity: DeltaEntity = { id: e.id, name: e.name }
                        return parsedEntity
                    }
                );
                setActiveDeltaEntites(newActiveDeltaEntities);
            });
    }, []);

    const handleSaveAfterEdit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (editEntityId === null) {
            return;
        }

        const formData = new FormData(event.currentTarget);
        const newName: string = formData.get("name") as string;

        axios.patch(URL + '/patch/entity', { id: editEntityId, name: newName })
            .then(response => {
                if (response.data === 'done') {
                    const updatedActiveDeltaEntites = activeDeltaEntites.map((entity) => {
                        if (entity.id === editEntityId) {
                            const updatedEntity: DeltaEntity = { id: entity.id, name: newName }
                            return updatedEntity;
                        }
                        return entity;
                    });

                    setActiveDeltaEntites(updatedActiveDeltaEntites);
                }

                setEditEntityId(null);
            });

    }

    const handleDeleteEntity = (id: number) => {
        axios.delete(URL + '/delete/entity?id=' + id)
            .then(response => {
                if (response.data === 'done') {
                    const updatedActiveDeltaEntites = activeDeltaEntites.filter((entity) => entity.id !== id);
                    setActiveDeltaEntites(updatedActiveDeltaEntites);
                }
            });
    };

    const DeltaEntityAddForm = () => {
        const [showForm, setShowForm] = useState(false);
        const [id, setId] = useState("");
        const [name, setName] = useState("");
        const [error, setError] = useState("");

        const handleAddClick = () => {
            setId("");
            setName("");
            setError("");
            setShowForm(true);
        };

        const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
            e.preventDefault();
            const newEntity = { id: Number(id), name };

            axios.post(URL + '/create/entity', newEntity)
                .then(response => {
                    if (response.data === 'done') {
                        activeDeltaEntites.push(newEntity);
                        const updatedActiveDeltaEntites = activeDeltaEntites.slice();
                        setActiveDeltaEntites(updatedActiveDeltaEntites);

                        setId("");
                        setName("");
                        setError("");
                        setShowForm(false);
                    } else {
                        setError(response.data);
                    }
                });
        };

        const handleIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setId(e.target.value);
        };

        const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setName(e.target.value);
        };

        if (!showForm) {
            return (
                <button onClick={handleAddClick}>Add Entity</button>
            );
        }

        return (
            <form className='add_form' onSubmit={handleSubmit}>
                <div>
                    <label>
                        Id:
                        <input type="number" value={id} onChange={handleIdChange} />
                    </label>
                </div>
                <div>
                    <label>
                        Name:
                        <input type="text" value={name} onChange={handleNameChange} />
                    </label>
                </div>
                <div>
                    <button className="add_button_form" type="submit">Add</button>
                    <button type="button" onClick={() => setShowForm(false)}>Cancel</button>
                </div>
                {error && <p className='error_msg'>{error}</p>}
            </form>
        );
    }

    return (
        <div className='tables delta_tables'>
            <div className='flex_tables_wrapper'>
                <div className='active_table'>
                    <p style={{textDecoration: "underline"}}>Delta entities</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {activeDeltaEntites.map((entity) => (
                                <tr key={entity.id}>
                                    <td>{entity.id}</td>
                                    {editEntityId === entity.id ? (
                                        <td>
                                            <form onSubmit={handleSaveAfterEdit}>
                                                <input
                                                    type="text"
                                                    name="name"
                                                    defaultValue={entity.name}
                                                />
                                                <button type="submit">Save</button>
                                            </form>
                                        </td>
                                    ) : (
                                        <td>{entity.name}</td>
                                    )}
                                    <td>
                                        <button onClick={() => setEditEntityId(entity.id)}>
                                            Edit
                                        </button>
                                        <button onClick={() => handleDeleteEntity(entity.id)}>
                                            Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <DeltaEntityAddForm />
                </div>
            </div>
        </div>

    );
}

export default Delta;