import axios from 'axios';
import React, { useState, useEffect } from 'react';
import { BACKEND_URL, isCustomNumeric } from '../Utils';

type GammaToDeltaLink = {
    gammaId: number,
    deltaId: number
}

const URL: string = BACKEND_URL + 'external/v1/gamma';

function GammaToDeltaLinkTables() {
    const [activeGammaToDeltaLinks, setActiveGammaToDeltaLinks] = useState<GammaToDeltaLink[]>([]);
    const [disableGammaToDeltaLinks, setDisableGammaToDeltaLinks] = useState<GammaToDeltaLink[]>([]);

    useEffect(() => {
        axios.get(URL + '/link/delta/active')
            .then(response => {
                const newActiveGammaToDeltaLinks: GammaToDeltaLink[] = response.data.map(
                    (e: any) => {
                        const parsedLink: GammaToDeltaLink = { gammaId: e.gammaId, deltaId: e.deltaId }
                        return parsedLink
                    }
                );
                setActiveGammaToDeltaLinks(newActiveGammaToDeltaLinks);
            });

        axios.get(URL + '/link/delta/disable')
            .then(response => {
                const disableGammaToDeltaLinks: GammaToDeltaLink[] = response.data.map(
                    (e: any) => {
                        const parsedLink: GammaToDeltaLink = { gammaId: e.gammaId, deltaId: e.deltaId }
                        return parsedLink
                    }
                );
                setDisableGammaToDeltaLinks(disableGammaToDeltaLinks);
            });
    }, []);

    const hadleSwitchActivity = (gammaId: number, deltaId: number, isSwitchToActive: boolean) => {
        axios.post(URL + '/switch/link/delta?gammaId=' + gammaId + '&deltaId=' + deltaId)
            .then(response => {
                if (response.data === 'done') {
                    let updatedActiveGammaToDeltaLinks: GammaToDeltaLink[] = [];
                    let updatedDisableGammaToDeltaLinks: GammaToDeltaLink[] = [];

                    if (isSwitchToActive) {
                        for (const entity of disableGammaToDeltaLinks) {
                            if (entity.gammaId === gammaId && entity.deltaId == deltaId) {
                                activeGammaToDeltaLinks.push(entity);
                                break;
                            }
                        }

                        updatedActiveGammaToDeltaLinks = activeGammaToDeltaLinks;
                        updatedDisableGammaToDeltaLinks = disableGammaToDeltaLinks
                            .filter((entity) => !(entity.gammaId === gammaId && entity.deltaId === deltaId));
                    } else {
                        for (const entity of activeGammaToDeltaLinks) {
                            if (entity.gammaId === gammaId && entity.deltaId == deltaId) {
                                disableGammaToDeltaLinks.push(entity);
                                break;
                            }
                        }

                        updatedDisableGammaToDeltaLinks = disableGammaToDeltaLinks;
                        updatedActiveGammaToDeltaLinks = activeGammaToDeltaLinks
                            .filter((entity) => !(entity.gammaId === gammaId && entity.deltaId === deltaId));
                    }

                    setActiveGammaToDeltaLinks(updatedActiveGammaToDeltaLinks);
                    setDisableGammaToDeltaLinks(updatedDisableGammaToDeltaLinks);
                }
            });
    };

    const GammaToDeltaLinkAddForm = () => {
        const [showForm, setShowForm] = useState(false);
        const [gammaId, setGammaId] = useState(0);
        const [deltaId, setDeltaId] = useState(0);
        const [error, setError] = useState("");

        const handleAddClick = () => {
            setGammaId(0);
            setDeltaId(0);
            setError("");
            setShowForm(true);
        };

        const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
            e.preventDefault();
            const newEntity: GammaToDeltaLink = { gammaId: gammaId, deltaId: deltaId };

            axios.post(URL + '/create/link/delta', newEntity)
                .then(response => {
                    if (response.data === 'done') {
                        activeGammaToDeltaLinks.push(newEntity);
                        const updatedActiveGammaToDeltaLinks = activeGammaToDeltaLinks.slice();
                        setActiveGammaToDeltaLinks(updatedActiveGammaToDeltaLinks);

                        setGammaId(0);
                        setDeltaId(0);
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

        const handleDeltaIdChange = (e: React.ChangeEvent<HTMLInputElement>) => {
            setDeltaId(Number(e.target.value));
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
                        Delta Id:
                        <input type="text" value={deltaId} onChange={handleDeltaIdChange} />
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
            <p className='entities_type'>Gamma - Delta Links</p>
            <div className='flex_tables_wrapper'>
                <div className='active_table'>
                    <p>Active links</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Gamma Id</th>
                                <th>Delta Id</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {activeGammaToDeltaLinks.map((link) => (
                                <tr key={link.gammaId + "_" + link.deltaId}>
                                    <td>{link.gammaId}</td>
                                    <td>{link.deltaId}</td>
                                    <td>
                                        <button onClick={() => hadleSwitchActivity(link.gammaId, link.deltaId, false)}>
                                            Make disabled
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <GammaToDeltaLinkAddForm />
                </div>

                <div className='disable_table'>
                    <p>Disable links</p>
                    <table>
                        <thead>
                            <tr>
                                <th>Gamma Id</th>
                                <th>Delta Id</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {disableGammaToDeltaLinks.map((link) => (
                                <tr key={link.gammaId + "_" + link.deltaId}>
                                    <td>{link.gammaId}</td>
                                    <td>{link.deltaId}</td>
                                    <td>
                                        <button onClick={() => hadleSwitchActivity(link.gammaId, link.deltaId, true)}>
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

export default GammaToDeltaLinkTables;