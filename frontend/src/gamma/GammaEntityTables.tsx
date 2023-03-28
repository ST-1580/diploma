import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { BACKEND_URL } from '../Utils';

type GammaEntity = {
    id: number,
    isMaster: boolean
}

const URL: string = BACKEND_URL + 'external/v1/gamma';


function GammaEntiyTables() {
    const [activeGammaEntites, setActiveGammaEntites] = useState<GammaEntity[]>([]);
    const [disableGammaEntites, setDisableGammaEntites] = useState<GammaEntity[]>([]);
    const [editEntityId, setEditEntityId] = useState<number | null>(null);

    useEffect(() => {
        axios.get(URL + '/entities/active')
            .then(response => {
                const newActiveGammaEntities: GammaEntity[] = response.data.map(
                    (e: any) => {
                        const parsedEntity: GammaEntity = { id: e.id, isMaster: e.isMaster}
                        return parsedEntity
                    }
                );
                setActiveGammaEntites(newActiveGammaEntities);
            });

        axios.get(URL + '/entities/disable')
            .then(response => {
                const disableGammaEntities: GammaEntity[] = response.data.map(
                    (e: any) => {
                        const parsedEntity: GammaEntity = { id: e.id, isMaster: e.isMaster}
                        return parsedEntity
                    }
                );
                setDisableGammaEntites(disableGammaEntities);
            });
    }, []);

    const handleSaveAfterEdit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (editEntityId === null) {
            return;
        }

        const formData = new FormData(event.currentTarget);
        const newMaster: boolean = Boolean(formData.get("isMaster"));

        axios.patch(URL + '/patch/entity', { id: editEntityId, isMaster: newMaster })
            .then(response => {
                if (response.data === 'done') {
                    const updatedActiveGammaEntites = activeGammaEntites.map((entity) => {
                        if (entity.id === editEntityId) {
                            const updatedEntity: GammaEntity = { id: entity.id, isMaster: newMaster}
                            return updatedEntity;
                        }
                        return entity;
                    });

                    setActiveGammaEntites(updatedActiveGammaEntites);
                }

                setEditEntityId(null);
            });

    }

    const hadleSwitchActivity = (id: number, isSwitchToActive: boolean) => {
        axios.post(URL + '/switch/entity?id=' + id)
            .then(response => {
                if (response.data === 'done') {
                    let updatedActiveGammaEntites: GammaEntity[] = [];
                    let updatedDisableGammaEntites: GammaEntity[] = [];

                    if (isSwitchToActive) {
                        for (const entity of disableGammaEntites) {
                            if (entity.id === id) {
                                activeGammaEntites.push(entity);
                                break;
                            }
                        }

                        updatedActiveGammaEntites = activeGammaEntites;
                        updatedDisableGammaEntites = disableGammaEntites.filter((entity) => entity.id !== id);
                    } else {
                        for (const entity of activeGammaEntites) {
                            if (entity.id === id) {
                                disableGammaEntites.push(entity);
                                break;
                            }
                        }

                        updatedDisableGammaEntites = disableGammaEntites;
                        updatedActiveGammaEntites = activeGammaEntites.filter((entity) => entity.id !== id);
                    }

                    setActiveGammaEntites(updatedActiveGammaEntites);
                    setDisableGammaEntites(updatedDisableGammaEntites);
                }
            });
    };

    const GammaEntityAddForm = () => {
        const [showForm, setShowForm] = useState(false);
        const [id, setId] = useState("");
        const [isMaster, setIsMaster] = useState(false)
        const [error, setError] = useState("");

        const handleAddClick = () => {
            setId("");
            setIsMaster(false);
            setError("");
            setShowForm(true);
        };

        const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
            e.preventDefault();
            const newEntity = { id: Number(id), isMaster };

            axios.post(URL + '/create/entity', newEntity)
                .then(response => {
                    if (response.data === 'done') {
                        activeGammaEntites.push(newEntity);
                        const updatedActiveGammaEntites = activeGammaEntites.slice();
                        setActiveGammaEntites(updatedActiveGammaEntites);

                        setId("");
                        setIsMaster(false);
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

        const handleIsMasterChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setIsMaster(Boolean(e.target.value));
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
                        Is Master:
                        <input type="checkbox" onChange={handleIsMasterChange} />
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
        <>
            <p className='entities_type'>Gamma Entites</p>
            <div className='flex_tables_wrapper'>
                <div className='active_table'>
                    <p>Active entities</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Is Master</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {activeGammaEntites.map((entity) => (
                                <tr key={entity.id}>
                                    <td>{entity.id}</td>
                                    {editEntityId === entity.id ? (
                                        <td>
                                            <form onSubmit={handleSaveAfterEdit}>
                                                <input
                                                    type="checkbox"
                                                    name="isMaster"
                                                    defaultChecked={entity.isMaster}
                                                />
                                                <button type="submit">Save</button>
                                            </form>
                                        </td>
                                    ) : (
                                        <td>{entity.isMaster ? 'true' : 'false'}</td>
                                    )}
                                    <td>
                                        <button onClick={() => setEditEntityId(entity.id)}>
                                            Edit
                                        </button>
                                        <button onClick={() => hadleSwitchActivity(entity.id, false)}>
                                            Disable
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <GammaEntityAddForm />
                </div>

                <div className='disable_table'>
                    <p>Disabled entities</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Is Master</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {disableGammaEntites.map((entity) => (
                                <tr key={entity.id}>
                                    <td>{entity.id}</td>
                                    <td>{entity.isMaster ? 'true' : 'false'}</td>
                                    <td>
                                        <button onClick={() => hadleSwitchActivity(entity.id, true)}>
                                            Enable
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    );
}

export default GammaEntiyTables;