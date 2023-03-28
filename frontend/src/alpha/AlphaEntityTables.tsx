import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { BACKEND_URL } from '../Utils';

type AlphaEntity = {
    id: number,
    name: string
}

const URL: string = BACKEND_URL + 'external/v1/alpha';


function AlphaEntiyTables() {
    const [activeAlphaEntites, setActiveAlphaEntites] = useState<AlphaEntity[]>([]);
    const [disableAlphaEntites, setDisableAlphaEntites] = useState<AlphaEntity[]>([]);
    const [editEntityId, setEditEntityId] = useState<number | null>(null);

    useEffect(() => {
        axios.get(URL + '/entities/active')
            .then(response => {
                const newActiveAlphaEntities: AlphaEntity[] = response.data.map(
                    (e: any) => {
                        const parsedEntity: AlphaEntity = { id: e.id, name: e.name }
                        return parsedEntity
                    }
                );
                setActiveAlphaEntites(newActiveAlphaEntities);
            });

        axios.get(URL + '/entities/disable')
            .then(response => {
                const disableAlphaEntities: AlphaEntity[] = response.data.map(
                    (e: any) => {
                        const parsedEntity: AlphaEntity = { id: e.id, name: e.name }
                        return parsedEntity
                    }
                );
                setDisableAlphaEntites(disableAlphaEntities);
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
                    const updatedActiveAlphaEntites = activeAlphaEntites.map((entity) => {
                        if (entity.id === editEntityId) {
                            const updatedEntity: AlphaEntity = { id: entity.id, name: newName }
                            return updatedEntity;
                        }
                        return entity;
                    });

                    setActiveAlphaEntites(updatedActiveAlphaEntites);
                }

                setEditEntityId(null);
            });

    }

    const hadleSwitchActivity = (id: number, isSwitchToActive: boolean) => {
        axios.post(URL + '/switch/entity?id=' + id)
            .then(response => {
                if (response.data === 'done') {
                    let updatedActiveAlphaEntites: AlphaEntity[] = [];
                    let updatedDisableAlphaEntites: AlphaEntity[] = [];

                    if (isSwitchToActive) {
                        for (const entity of disableAlphaEntites) {
                            if (entity.id === id) {
                                activeAlphaEntites.push(entity);
                                break;
                            }
                        }

                        updatedActiveAlphaEntites = activeAlphaEntites;
                        updatedDisableAlphaEntites = disableAlphaEntites.filter((entity) => entity.id !== id);
                    } else {
                        for (const entity of activeAlphaEntites) {
                            if (entity.id === id) {
                                disableAlphaEntites.push(entity);
                                break;
                            }
                        }

                        updatedDisableAlphaEntites = disableAlphaEntites;
                        updatedActiveAlphaEntites = activeAlphaEntites.filter((entity) => entity.id !== id);
                    }

                    setActiveAlphaEntites(updatedActiveAlphaEntites);
                    setDisableAlphaEntites(updatedDisableAlphaEntites);
                }
            });
    };

    const AlphaEntityAddForm = () => {
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
                        activeAlphaEntites.push(newEntity);
                        const updatedActiveAlphaEntites = activeAlphaEntites.slice();
                        setActiveAlphaEntites(updatedActiveAlphaEntites);

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
        <>
            <p className='entities_type'>Alpha Entites</p>
            <div className='flex_tables_wrapper'>
                <div className='active_table'>
                    <p>Active entities</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {activeAlphaEntites.map((entity) => (
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
                                        <button onClick={() => hadleSwitchActivity(entity.id, false)}>
                                            Disable
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <AlphaEntityAddForm />
                </div>

                <div className='disable_table'>
                    <p>Disabled entities</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Id</th>
                                <th>Name</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {disableAlphaEntites.map((entity) => (
                                <tr key={entity.id}>
                                    <td>{entity.id}</td>
                                    <td>{entity.name}</td>
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

export default AlphaEntiyTables;