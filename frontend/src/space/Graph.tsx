import React, { useRef, useEffect } from 'react';
import * as d3 from 'd3';
import { type } from 'os';
import { Link } from 'react-router-dom';

type Node = {
    id: string,
    name: string,
    group: string
}

type Link = {
    source: string,
    target: string
}

type D3Graph = {
    nodes: Node[],
    links: Link[]
}

const Graph: React.FC<any> = (data) => {
    useEffect(() => {
        const jsonUrl: string = data.jsonUrl;

        var margin = { top: 10, right: 30, bottom: 30, left: 30 },
            width = 1200 - margin.left - margin.right,
            height = 800 - margin.top - margin.bottom;

        d3.selectAll("svg").remove();

        var svg = d3.select("#space_graph")
            .append("svg")
            .attr("width", width)
            .attr("height", height)
            .append("g")
            .attr("transform",
                "translate(" + margin.left + "," + margin.top + ")");

        function getNewId(type: string, id: number): string {
            return type + "__" + id;
        }

        function convertData(oldJson: any): D3Graph {
            const d3Graph: D3Graph = { nodes: [], links: [] };
            d3Graph.nodes = oldJson.entities
                .map((e: any) => {
                    const newNode: Node = {
                        id: getNewId(e.type, e.id),
                        name: e.id,
                        group: e.type
                    }
                    return newNode
                });
            d3Graph.links = oldJson.links
                .map((l: any) => {
                    const newLink: Link = {
                        source: getNewId(l.fromGraphEntity.type, l.fromGraphEntity.id),
                        target: getNewId(l.toGraphEntity.type, l.toGraphEntity.id)
                    }
                    return newLink
                });

            return d3Graph;
        }

        function colorByGroup(group: string) {
            switch (group) {
                case "ALPHA":
                    return "#D5E8D4";
                case "BETA":
                    return "#DAE8FC";
                case "GAMMA":
                    return "#FFF2CC";
                case "DELTA":
                    return "#E1D5E7";
            }

            return "transparent"
        }

        d3.json(jsonUrl).then(
            (data: any) => {
                data = convertData(data);

                var link = svg
                    .selectAll("line")
                    .data(data.links)
                    .enter()
                    .append("line")
                    .style("fill", "transparent")

                var node = svg
                    .selectAll("circle")
                    .data(data.nodes)
                    .enter()
                    .append("circle")
                    .attr("r", 20)
                    .style("fill", "transparent")

                var label = svg.selectAll("text")
                    .data(data.nodes)
                    .enter()
                    .append("text")
                    .text(function (d: any) { return d.name; })
                    .style("text-anchor", "middle")
                    .style("font-family", "Arial")
                    .style("font-size", 0);

               
                d3.forceSimulation(data.nodes)               
                    .force("link", d3.forceLink()                              
                        .id(function (d: any) { return d.id; })                  
                        .links(data.links)                                   
                    )
                    .force("collide", d3.forceCollide().radius(40))
                    .force("center", d3.forceCenter(width / 2, height / 2))
                    .on("end", ticked);


                function ticked() {
                    link
                        .attr("x1", function (d: any) { return d.source.x; })
                        .attr("y1", function (d: any) { return d.source.y; })
                        .attr("x2", function (d: any) { return d.target.x; })
                        .attr("y2", function (d: any) { return d.target.y; })
                        .style("stroke", "#222");

                    node
                        .attr("cx", function (d: any) { return d.x + 6; })
                        .attr("cy", function (d: any) { return d.y - 6; })
                        .style("fill", function (d: any) { return colorByGroup(d.group) });

                    label
                        .attr("x", function (d: any) { return d.x + 6; })
                        .attr("y", function (d: any) { return d.y - 3; })
                        .style("font-size", "12px");
                }

            });
    }, []);

    return (
        <div id="space_graph"></div>
    );
};

export default Graph;