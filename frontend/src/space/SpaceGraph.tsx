import React, { useRef, useEffect } from 'react';
import axios from "axios";
import forceAtlas2 from 'graphology-layout-forceatlas2';
import Graph from 'graphology';
import Sigma from "sigma";
import { Link } from 'react-router-dom';

type Link = {
    source: string,
    target: string
}

const SpaceGraph: React.FC<any> = (data) => {
    useEffect(() => {
    const jsonUrl: string = data.jsonUrl;
        const container = document.getElementById("space_graph") as HTMLElement;

        function convertData(oldJson: any): Graph {
            function getNewId(type: string, id: number): string {
                return type + "__" + id;
            }

            function colorByGroup(group: string): string {
                switch (group) {
                    case "ALPHA":
                        return "#12b84f";
                    case "BETA":
                        return "#0088cc";
                    case "GAMMA":
                        return "orange";
                    case "DELTA":
                        return "purple";
                }
    
                return "transparent"
            }

            function createNodeLabel(id: string, payload: string): string {
                var res = "id: " + id;
                if (payload !== null) {
                    res += "; payload: " + JSON.stringify(payload);
                }
                return res;
            }

            function createEdgeLabel(payload: string): string {
                return payload === null ? "" : JSON.stringify(payload);
            }

            const graph: Graph = new Graph()
            oldJson.entities
                .map((e: any) => {
                    graph.addNode(getNewId(e.type, e.id), {x: Math.random(), y: Math.random(), size: 20, label: createNodeLabel(e.id, e.payload), color: colorByGroup(e.type)})
                });
            oldJson.links
                .map((l: any) => {
                    const newLink: Link = {
                        source: getNewId(l.fromGraphEntity.type, l.fromGraphEntity.id),
                        target: getNewId(l.toGraphEntity.type, l.toGraphEntity.id)
                    }
                    graph.addEdge(newLink.source, newLink.target, {label: createEdgeLabel(l.payload)})
                });
            
            forceAtlas2.assign(graph, { iterations: 50, settings: { gravity: 1 } });
            return graph;
        }

        axios.get(jsonUrl)
            .then(response => {
                const graph: Graph = convertData(response.data)
                new Sigma(graph, container, { renderLabels: false, renderEdgeLabels: true });
            });
    }, []);

    return (
        <div id="space_graph"></div>
    );
};

export default SpaceGraph;