import "./Alpha.css";
import AlphaEntiyTables from './AlphaEntityTables';
import AlphaToBetaLinkTables from "./AlphaToBetaLinkTables";

function Alpha() {
    return (
        <div className='tables alpha_tables'>
            <AlphaEntiyTables />
            <AlphaToBetaLinkTables />
        </div>

    )
}

export default Alpha;