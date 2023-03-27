import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { BACKEND_URL } from '../Utils';

type AlphaToBetaLink = {
    alphaId: number,
    betaId: number
    hash: string
}

type AlphaBetaId = {
    alphaId: number,
    betaId: number
}

const URL: string = BACKEND_URL + 'external/v1/alpha';

function AlphaToBetaLinkTables() {
    const [activeAlphaToBetaLinks, setActiveAlphaToBetaLinks] = useState<AlphaToBetaLink[]>([]);
    const [disableAlphaToBetaLinks, setDisableAlphaToBetaLinks] = useState<AlphaToBetaLink[]>([]);
    const [editLinkId, setEditLinkId] = useState<AlphaBetaId | null>(null);

    useEffect(() => {
        axios.get(URL + '/link/beta/active')
            .then(response => {
                const newActiveAlphaToBetaLinks: AlphaToBetaLink[] = response.data.map(
                    (e: any) => {
                        const parsedLink: AlphaToBetaLink = { alphaId: e.alphaId, betaId: e.betaId, hash: e.hash }
                        return parsedLink
                    }
                );
                setActiveAlphaToBetaLinks(newActiveAlphaToBetaLinks);
            });

        axios.get(URL + '/link/beta/disable')
            .then(response => {
                const disableAlphaToBetaLinks: AlphaToBetaLink[] = response.data.map(
                    (e: any) => {
                        const parsedLink: AlphaToBetaLink = { alphaId: e.alphaId, betaId: e.betaId, hash: e.hash }
                        return parsedLink
                    }
                );
                setDisableAlphaToBetaLinks(disableAlphaToBetaLinks);
            });
    }, []);

    const handleSaveAfterEdit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        if (editLinkId === null) {
            return;
        }

        const formData = new FormData(event.currentTarget);
        const newHash: string = formData.get("hash") as string;

        axios.patch(URL + '/patch/link/beta', { alphaId: editLinkId.alphaId, betaId: editLinkId.betaId, hash: newHash })
            .then(response => {
                if (response.data === 'done') {
                    const updatedActiveAlphaToBetaLinks = activeAlphaToBetaLinks.map((entity) => {
                        if (entity.alphaId === editLinkId.alphaId && entity.betaId == editLinkId.betaId) {
                            const updatedEntity: AlphaToBetaLink = { alphaId: entity.alphaId, betaId: entity.betaId, hash: newHash }
                            return updatedEntity;
                        }
                        return entity;
                    });

                    setActiveAlphaToBetaLinks(updatedActiveAlphaToBetaLinks);
                }

                setEditLinkId(null);
            });

    }

    const hadleSwitchActivity = (alphaId: number, betaId: number, isSwitchToActive: boolean) => {
        axios.post(URL + '/switch/link/beta?alphaId=' + alphaId + '&betaId=' + betaId)
            .then(response => {
                if (response.data === 'done') {
                    let updatedActiveAlphaToBetaLinks: AlphaToBetaLink[] = [];
                    let updatedDisableAlphaToBetaLinks: AlphaToBetaLink[] = [];

                    if (isSwitchToActive) {
                        for (const entity of disableAlphaToBetaLinks) {
                            if (entity.alphaId === alphaId && entity.betaId == betaId) {
                                activeAlphaToBetaLinks.push(entity);
                                break;
                            }
                        }

                        updatedActiveAlphaToBetaLinks = activeAlphaToBetaLinks;
                        updatedDisableAlphaToBetaLinks = disableAlphaToBetaLinks
                            .filter((entity) => !(entity.alphaId === alphaId && entity.betaId === betaId));
                    } else {
                        for (const entity of activeAlphaToBetaLinks) {
                            if (entity.alphaId === alphaId && entity.betaId == betaId) {
                                disableAlphaToBetaLinks.push(entity);
                                break;
                            }
                        }

                        updatedDisableAlphaToBetaLinks = disableAlphaToBetaLinks;
                        updatedActiveAlphaToBetaLinks = activeAlphaToBetaLinks
                            .filter((entity) => !(entity.alphaId === alphaId && entity.betaId === betaId));
                    }

                    setActiveAlphaToBetaLinks(updatedActiveAlphaToBetaLinks);
                    setDisableAlphaToBetaLinks(updatedDisableAlphaToBetaLinks);
                }
            });
    };

    const AlphaToBetaLinkAddForm = () => {
        const [showForm, setShowForm] = useState(false);
        const [alphaId, setAlphaId] = useState(0);
        const [betaId, setBetaId] = useState(0);
        const [hash, setHash] = useState("");
        const [error, setError] = useState("");

        const handleAddClick = () => {
            setAlphaId(0);
            setBetaId(0);
            setHash("");
            setError("");
            setShowForm(true);
        };

        const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
            e.preventDefault();
            const newEntity: AlphaToBetaLink = { alphaId: alphaId, betaId: betaId, hash: hash };

            axios.post(URL + '/create/link/beta', newEntity)
                .then(response => {
                    if (response.data === 'done') {
                        activeAlphaToBetaLinks.push(newEntity);
                        const updatedActiveAlphaToBetaLinks = activeAlphaToBetaLinks.slice();
                        setActiveAlphaToBetaLinks(updatedActiveAlphaToBetaLinks);

                        setAlphaId(0);
                        setBetaId(0);
                        setHash("");
                        setError("");
                        setShowForm(false);
                    } else {
                        setError(response.data);
                    }
                });
        };

        const handleAlphaIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setAlphaId(Number(e.target.value));
        };

        const handleBetaIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setBetaId(Number(e.target.value));
        };

        const handleNameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setHash(e.target.value);
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
                        Alpha Id:
                        <input type="text" value={alphaId} onChange={handleAlphaIdChange} />
                    </label>
                </div>
                <div>
                    <label>
                        Beta Id:
                        <input type="text" value={betaId} onChange={handleBetaIdChange} />
                    </label>
                </div>
                <div>
                    <label>
                        Name:
                        <input type="text" value={hash} onChange={handleNameChange} />
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
            <p className='entities_type'>Alpha - Beta Links</p>
            <div className='flex_tables_wrapper'>
                <div className='active_table'>
                    <p>Active links</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Alpha Id</th>
                                <th>Beta Id</th>
                                <th>Hash</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {activeAlphaToBetaLinks.map((link) => (
                                <tr key={link.alphaId + "_" + link.betaId}>
                                    <td>{link.alphaId}</td>
                                    <td>{link.betaId}</td>
                                    {editLinkId?.alphaId === link.alphaId && editLinkId?.betaId == link.betaId ? (
                                        <td>
                                            <form onSubmit={handleSaveAfterEdit}>
                                                <input
                                                    type="text"
                                                    name="hash"
                                                    defaultValue={link.hash}
                                                />
                                                <button type="submit">Save</button>
                                            </form>
                                        </td>
                                    ) : (
                                        <td>{link.hash}</td>
                                    )}
                                    <td>
                                        <button onClick={() => setEditLinkId({ alphaId: link.alphaId, betaId: link.betaId })}>
                                            Edit
                                        </button>
                                        <button onClick={() => hadleSwitchActivity(link.alphaId, link.betaId, false)}>
                                            Make disabled
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <AlphaToBetaLinkAddForm />
                </div>

                <div className='disable_table'>
                    <p>Disable links</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Alpha Id</th>
                                <th>Beta Id</th>
                                <th>Hash</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {disableAlphaToBetaLinks.map((link) => (
                                <tr key={link.alphaId + "_" + link.betaId}>
                                    <td>{link.alphaId}</td>
                                    <td>{link.betaId}</td>
                                    <td>{link.hash}</td>
                                    <td>
                                        <button onClick={() => hadleSwitchActivity(link.alphaId, link.betaId, true)}>
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

export default AlphaToBetaLinkTables;