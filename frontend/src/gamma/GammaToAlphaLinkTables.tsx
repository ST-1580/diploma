import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { BACKEND_URL, isCustomNumeric } from '../Utils';

type GammaToAlphaLink = {
    gammaId: number,
    alphaId: number
    weight: number
}

type GammaAlphaId = {
    gammaId: number,
    alphaId: number
}

const URL: string = BACKEND_URL + 'external/v1/gamma';

function GammaToAlphaLinkTables() {
    const [activeGammaToAlphaLinks, setActiveGammaToAlphaLinks] = useState<GammaToAlphaLink[]>([]);
    const [disableGammaToAlphaLinks, setDisableGammaToAlphaLinks] = useState<GammaToAlphaLink[]>([]);
    const [editLinkId, setEditLinkId] = useState<GammaAlphaId | null>(null);

    useEffect(() => {
        axios.get(URL + '/link/alpha/active')
            .then(response => {
                const newActiveGammaToAlphaLinks: GammaToAlphaLink[] = response.data.map(
                    (e: any) => {
                        const parsedLink: GammaToAlphaLink = { gammaId: e.gammaId, alphaId: e.alphaId, weight: e.weight }
                        return parsedLink
                    }
                );
                setActiveGammaToAlphaLinks(newActiveGammaToAlphaLinks);
            });

        axios.get(URL + '/link/alpha/disable')
            .then(response => {
                const disableGammaToAlphaLinks: GammaToAlphaLink[] = response.data.map(
                    (e: any) => {
                        const parsedLink: GammaToAlphaLink = { gammaId: e.gammaId, alphaId: e.alphaId, weight: e.weight }
                        return parsedLink
                    }
                );
                setDisableGammaToAlphaLinks(disableGammaToAlphaLinks);
            });
    }, []);

    const handleSaveAfterEdit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (editLinkId === null) {
            return;
        }

        const formData = new FormData(event.currentTarget);
        const newWeight: number = Number(formData.get("weight"));

        axios.patch(URL + '/patch/link/alpha', { gammaId: editLinkId.gammaId, alphaId: editLinkId.alphaId, weight: newWeight })
            .then(response => {
                if (response.data === 'done') {
                    const updatedActiveGammaToAlphaLinks = activeGammaToAlphaLinks.map((entity) => {
                        if (entity.gammaId === editLinkId.gammaId && entity.alphaId == editLinkId.alphaId) {
                            const updatedEntity: GammaToAlphaLink = { gammaId: entity.gammaId, alphaId: entity.alphaId, weight: newWeight }
                            return updatedEntity;
                        }
                        return entity;
                    });

                    setActiveGammaToAlphaLinks(updatedActiveGammaToAlphaLinks);
                }

                setEditLinkId(null);
            });

    }

    const hadleSwitchActivity = (gammaId: number, alphaId: number, isSwitchToActive: boolean) => {
        axios.post(URL + '/switch/link/alpha?gammaId=' + gammaId + '&alphaId=' + alphaId)
            .then(response => {
                if (response.data === 'done') {
                    let updatedActiveGammaToAlphaLinks: GammaToAlphaLink[] = [];
                    let updatedDisableGammaToAlphaLinks: GammaToAlphaLink[] = [];

                    if (isSwitchToActive) {
                        for (const entity of disableGammaToAlphaLinks) {
                            if (entity.gammaId === gammaId && entity.alphaId == alphaId) {
                                activeGammaToAlphaLinks.push(entity);
                                break;
                            }
                        }

                        updatedActiveGammaToAlphaLinks = activeGammaToAlphaLinks;
                        updatedDisableGammaToAlphaLinks = disableGammaToAlphaLinks
                            .filter((entity) => !(entity.gammaId === gammaId && entity.alphaId === alphaId));
                    } else {
                        for (const entity of activeGammaToAlphaLinks) {
                            if (entity.gammaId === gammaId && entity.alphaId == alphaId) {
                                disableGammaToAlphaLinks.push(entity);
                                break;
                            }
                        }

                        updatedDisableGammaToAlphaLinks = disableGammaToAlphaLinks;
                        updatedActiveGammaToAlphaLinks = activeGammaToAlphaLinks
                            .filter((entity) => !(entity.gammaId === gammaId && entity.alphaId === alphaId));
                    }

                    setActiveGammaToAlphaLinks(updatedActiveGammaToAlphaLinks);
                    setDisableGammaToAlphaLinks(updatedDisableGammaToAlphaLinks);
                }
            });
    };

    const GammaToAlphaLinkAddForm = () => {
        const [showForm, setShowForm] = useState(false);
        const [gammaId, setGammaId] = useState(0);
        const [alphaId, setAlphaId] = useState(0);
        const [weight, setWeight] = useState(0);
        const [error, setError] = useState("");

        const handleAddClick = () => {
            setGammaId(0);
            setAlphaId(0);
            setWeight(0);
            setError("");
            setShowForm(true);
        };

        const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
            e.preventDefault();
            const newEntity: GammaToAlphaLink = { gammaId: gammaId, alphaId: alphaId, weight: weight };

            axios.post(URL + '/create/link/alpha', newEntity)
                .then(response => {
                    if (response.data === 'done') {
                        activeGammaToAlphaLinks.push(newEntity);
                        const updatedActiveGammaToAlphaLinks = activeGammaToAlphaLinks.slice();
                        setActiveGammaToAlphaLinks(updatedActiveGammaToAlphaLinks);

                        setGammaId(0);
                        setAlphaId(0);
                        setWeight(0);
                        setError("");
                        setShowForm(false);
                    } else {
                        setError(response.data);
                    }
                });
        };

        const handleGammaIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setGammaId(Number(e.target.value))
        };

        const handleAlphaIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setAlphaId(Number(e.target.value));
        };

        const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setWeight(Number(e.target.value));
        };

        if (!showForm) {
            return (
                <button onClick={handleAddClick}>Add Link</button>
            );
        }

        return (
            <form className='add_form' onSubmit={handleSubmit}>
                <div>
                    <label>
                        Gamma Id:
                        <input type="text" value={gammaId} onChange={handleGammaIdChange} />
                    </label>
                </div>
                <div>
                    <label>
                        Alpha Id:
                        <input type="text" value={alphaId} onChange={handleAlphaIdChange} />
                    </label>
                </div>
                <div>
                    <label>
                        Weight:
                        <input type="text" value={weight} onChange={handleNameChange} />
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
            <p className='entities_type'>Gamma - Alpha Links</p>
            <div className='flex_tables_wrapper'>
                <div className='active_table'>
                    <p>Active links</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Gamma Id</th>
                                <th>Alpha Id</th>
                                <th>Weight</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {activeGammaToAlphaLinks.map((link) => (
                                <tr key={link.gammaId + "_" + link.alphaId}>
                                    <td>{link.gammaId}</td>
                                    <td>{link.alphaId}</td>
                                    {editLinkId?.gammaId === link.gammaId && editLinkId?.alphaId == link.alphaId ? (
                                        <td>
                                            <form onSubmit={handleSaveAfterEdit}>
                                                <input
                                                    type="text"
                                                    name="weight"
                                                    defaultValue={link.weight}
                                                />
                                                <button type="submit">Save</button>
                                            </form>
                                        </td>
                                    ) : (
                                        <td>{link.weight}</td>
                                    )}
                                    <td>
                                        <button onClick={() => setEditLinkId({ gammaId: link.gammaId, alphaId: link.alphaId })}>
                                            Edit
                                        </button>
                                        <button onClick={() => hadleSwitchActivity(link.gammaId, link.alphaId, false)}>
                                            Make disabled
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <GammaToAlphaLinkAddForm />
                </div>

                <div className='disable_table'>
                    <p>Disable links</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Gamma Id</th>
                                <th>Alpha Id</th>
                                <th>Weight</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {disableGammaToAlphaLinks.map((link) => (
                                <tr key={link.gammaId + "_" + link.alphaId}>
                                    <td>{link.gammaId}</td>
                                    <td>{link.alphaId}</td>
                                    <td>{link.weight}</td>
                                    <td>
                                        <button onClick={() => hadleSwitchActivity(link.gammaId, link.alphaId, true)}>
                                            Make active
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

export default GammaToAlphaLinkTables;