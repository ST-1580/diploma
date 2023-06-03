import React, { useState } from 'react';
import axios from 'axios';
import * as d3 from "d3";
import "./Space.css";
import { BACKEND_URL } from '../Utils';
import SpaceGraph from './SpaceGraph';

const URL: string = BACKEND_URL + 'v1/';

type EntityType = 'alpha' | 'beta' | 'gamma' | 'delta';

interface FormValues {
    entityType: EntityType;
    entityId: number;
    policy: 'start' | 'non_cyclic';
    isLinksLight: boolean;
    isEntitesLight: boolean;
    timestampType: 'now' | 'other';
    timestampValue?: number;
}

function Space() {
    const [formValues, setFormValues] = useState<FormValues>({
        entityType: 'alpha',
        entityId: 0,
        policy: 'start',
        isLinksLight: true,
        isEntitesLight: true,
        timestampType: 'now',
    });
    const [isFormVisible, setIsFormVisible] = useState<boolean>(false)
    const [backendUrl, setBackendUrl] = useState<string>("")

    const handleSettingClick = () => {
        setIsFormVisible(true);
    }

    const handleInputChange = (event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
        const eventTarget = event.target;
        const name = eventTarget.name;

        if (eventTarget.type === "checkbox") {
            setFormValues((prevValues) => ({
                ...prevValues,
                [name]: Boolean((eventTarget as any).checked),
            }));
        } else if (eventTarget.name == "timestampType" && eventTarget.value == "now") {
            setFormValues((prevValues) => ({
                ...prevValues,
                [name]: eventTarget.value,
                ["timestampValue"]: undefined,
            }));
        } else {
            setFormValues((prevValues) => ({
                ...prevValues,
                [name]: eventTarget.value,
            }));
        }  
    };

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        const ts: number = formValues.timestampValue == null ? -1 : formValues.timestampValue;
        const final_url = URL + formValues.entityType + '/all/' + formValues.entityId +
            '?ts=' + ts + '&policy=' + formValues.policy + '&ll=' + formValues.isLinksLight +
            '&le=' + formValues.isEntitesLight;

        setBackendUrl(final_url)
        setIsFormVisible(false);
    };

    return (
        <div className="space_form">
            {isFormVisible ? (
                <form onSubmit={handleSubmit}>
                    <div>
                        <label>
                            Entity Type:
                            <select name="entityType" value={formValues.entityType} onChange={handleInputChange}>
                                <option value="alpha">Alpha</option>
                                <option value="beta">Beta</option>
                                <option value="gamma">Gamma</option>
                                <option value="delta">Delta</option>
                            </select>
                        </label>
                    </div>
                    <div>
                        <label>
                            Entity ID:
                            <input type="number" name="entityId" value={formValues.entityId} onChange={handleInputChange} />
                        </label>
                    </div>
                    <div>
                        <label>
                            Policy:
                            <select name="policy" value={formValues.policy} onChange={handleInputChange}>
                                <option value="start">Start</option>
                                <option value="non_cyclic">Non Cyclic</option>
                            </select>
                        </label>
                    </div>
                    <div>
                        <label>
                            Is Links Light:
                            <input type="checkbox" name="isLinksLight" defaultChecked={formValues.isLinksLight} onChange={handleInputChange} />
                        </label>
                    </div>
                    <div>
                        <label>
                            Is Entities Light:
                            <input type="checkbox" name="isEntitesLight" defaultChecked={formValues.isEntitesLight} onChange={handleInputChange} />
                        </label>
                    </div>
                    <div>
                        <label>
                            Timestamp Type:
                            <select name="timestampType" value={formValues.timestampType} onChange={handleInputChange}>
                                <option value="now">Now</option>
                                <option value="other">Other</option>
                            </select>
                        </label>
                    </div>
                    {formValues.timestampType === 'other' && (
                        <div>
                            <label>
                                Timestamp Value:
                                <input
                                    type="number"
                                    name="timestampValue"
                                    value={formValues.timestampValue}
                                    onChange={handleInputChange}
                                />
                            </label>
                        </div>
                    )}
                    <button type="submit">Generate</button>
                </form>
            ) : (
                <>
                    <button onClick={handleSettingClick}>Set generate configuration</button>
                    <SpaceGraph jsonUrl={backendUrl}/>
                </>
            )}
        </div>
    );
}

export default Space;