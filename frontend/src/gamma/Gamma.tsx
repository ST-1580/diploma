import "./Gamma.css";
import GammaEntityTables from "./GammaEntityTables";
import GammaToAlphaLinkTables from "./GammaToAlphaLinkTables";
import GammaToDeltaLinkTables from "./GammaToDeltaLinkTables";

function Gamma() {
    return (
        <div className='tables gamma_tables'>
            <GammaEntityTables />
            <GammaToAlphaLinkTables />
            <GammaToDeltaLinkTables />
        </div>

    )
}

export default Gamma;