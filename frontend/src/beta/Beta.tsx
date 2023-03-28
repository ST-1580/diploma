import "./Beta.css";
import axios from "axios";
import React, { useState, useEffect } from "react";
import { BACKEND_URL } from "../Utils";

type BetaEntity = {
    id: number,
    epoch: number
}

const URL: string = BACKEND_URL + 'external/v1/beta';

function Beta() {
    const [activeBetaEntites, setActiveBetaEntites] = useState<BetaEntity[]>([]);
    const [editEntityId, setEditEntityId] = useState<number | null>(null);

    useEffect(() => {
        axios.get(URL + '/entities/active')
            .then(response => {
                const newActiveBetaEntities: BetaEntity[] = response.data.map(
                    (e: any) => {
                        const parsedEntity: BetaEntity = { id: e.id, epoch: e.epoch }
                        return parsedEntity
                    }
                );
                setActiveBetaEntites(newActiveBetaEntities);
            });
    }, []);

    const handleSaveAfterEdit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (editEntityId === null) {
            return;
        }

        const formData = new FormData(event.currentTarget);
        const newEpoch: number = Number(formData.get("epoch"));

        axios.patch(URL + '/patch/entity', { id: editEntityId, epoch: newEpoch })
            .then(response => {
                if (response.data === 'done') {
                    const updatedActiveBetaEntites = activeBetaEntites.map((entity) => {
                        if (entity.id === editEntityId) {
                            const updatedEntity: BetaEntity = { id: entity.id, epoch: newEpoch }
                            return updatedEntity;
                        }
                        return entity;
                    });

                    setActiveBetaEntites(updatedActiveBetaEntites);
                }

                setEditEntityId(null);
            });

    }

    const handleDeleteEntity = (id: number) => {
        axios.delete(URL + '/delete/entity?id=' + id)
            .then(response => {
                if (response.data === 'done') {
                    const updatedActiveBetaEntites = activeBetaEntites.filter((entity) => entity.id !== id);
                    setActiveBetaEntites(updatedActiveBetaEntites);
                }
            });
    };

    const BetaEntityAddForm = () => {
        const [showForm, setShowForm] = useState(false);
        const [id, setId] = useState("");
        const [epoch, setEpoch] = useState("");
        const [error, setError] = useState("");

        const handleAddClick = () => {
            setId("");
            setEpoch("");
            setError("");
            setShowForm(true);
        };

        const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
            e.preventDefault();
            const newEntity = { id: Number(id), epoch: Number(epoch) };

            axios.post(URL + '/create/entity', newEntity)
                .then(response => {
                    if (response.data === 'done') {
                        activeBetaEntites.push(newEntity);
                        const updatedActiveBetaEntites = activeBetaEntites.slice();
                        setActiveBetaEntites(updatedActiveBetaEntites);

                        setId("");
                        setEpoch("");
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

        const handleEpochChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setEpoch(e.target.value);
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
                        Epoch:
                        <input type="number" value={epoch} onChange={handleEpochChange} />
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
        <div className='tables beta_tables'>
            <div className='flex_tables_wrapper'>
                <div className='active_table'>
                    <p style={{textDecoration: "underline"}}>Beta entities</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Epoch</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {activeBetaEntites.map((entity) => (
                                <tr key={entity.id}>
                                    <td>{entity.id}</td>
                                    {editEntityId === entity.id ? (
                                        <td>
                                            <form onSubmit={handleSaveAfterEdit}>
                                                <input
                                                    type="number"
                                                    name="epoch"
                                                    defaultValue={entity.epoch}
                                                />
                                                <button type="submit">Save</button>
                                            </form>
                                        </td>
                                    ) : (
                                        <td>{entity.epoch}</td>
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
                    <BetaEntityAddForm />
                </div>
            </div>
        </div>

    );
}

export default Beta;