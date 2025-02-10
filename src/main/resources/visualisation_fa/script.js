// Configuration constants
const CONFIG = {
    colors: {
        start: '#00539f',
        final: '#e91939',   
        startandfinal: "#a94c92",
        general: '#4d717c',
        border: '#374151',
        text: 'white'
    },
    sizes: {
        node: 70,
        font: 14,
        border: 2
    }
};


const createNode = (state) => {
    const nodeColors = {
        start: CONFIG.colors.start,
        final: CONFIG.colors.final,
        general: CONFIG.colors.general,
        startandfinal: CONFIG.colors.startandfinal
    };

    return {
        id: state.label,
        label: state.label,
        shape: 'circle',
        color: {
            background: nodeColors[state.type] || CONFIG.colors.general,
            border: CONFIG.colors.border
        },
        size: CONFIG.sizes.node,
        font: {
            size: CONFIG.sizes.font,
            color: CONFIG.colors.text,
            face: 'Inter, system-ui, sans-serif'
        },
        borderWidth: CONFIG.sizes.border
    };
};

// Edge creation helper function
const createEdge = (transition) => ({
    from: transition.from,
    to: transition.to,
    label: transition.label,
    arrows: 'to',
    color: CONFIG.colors.border,
    width: CONFIG.sizes.border,
    smooth: {
        type: 'curvedCW',
        roundness: 0.2
    },
    font: {
        size: CONFIG.sizes.font,
        color: 'black',
        face: 'Inter, system-ui, sans-serif'
    }
});

// Network options
// Network options
const createNetworkOptions = () => ({
    nodes: {
        shape: 'circle',
        font: {
            size: CONFIG.sizes.font,
            color: CONFIG.colors.text,
            face: 'Inter, system-ui, sans-serif'
        },
        size: CONFIG.sizes.node,
        borderWidth: CONFIG.sizes.border,
        fixed: { x: false, y: false },  // Allow dragging but don't change appearance
    },
    edges: {
        arrows: 'to',
        color: CONFIG.colors.border,
        width: CONFIG.sizes.border,
        smooth: {
            type: 'curvedCW',
            roundness: 0.2
        }
    },
    physics: {
        enabled: true,
        solver: 'forceAtlas2Based',
        forceAtlas2Based: {
            gravitationalConstant: -50,
            springLength: 100,
            springConstant: 0.08,
            damping: 0.4
        },
        stabilization: {
            enabled: true,
            iterations: 100,
            updateInterval: 25
        }
    },
    interaction: {
        dragNodes: true,  // Allow drag-and-drop
        dragView: true,
        zoomView: true,
        selectable: false,
        hover: false,
        multiselect: false,
        keyboard: false
    },
    manipulation: {
        enabled: false  // Disable node manipulation (add/remove) but allow DND
    }
});


// Initialize network
const initializeNetwork = async () => {
    try {
        const response = await fetch('data.json');
        if (!response.ok) throw new Error('Network response was not ok');
        
        const data = await response.json();
        const container = document.getElementById('mynetwork');
        
        const nodes = data.states.map(createNode);
        const edges = data.transitions.map(createEdge);
        
        const network = new vis.Network(
            container,
            { nodes, edges },
            createNetworkOptions()
        );

        network.on('dragEnd', function(){
            network.unselectAll();
          });


        // Disable physics after initial stabilization
        network.once('stabilizationIterationsDone', () => {
            network.setOptions({ physics: { enabled: true } });
        });

        return network;

    } catch (error) {
        console.error('Error initializing network:', error);
        const container = document.getElementById('mynetwork');
        container.innerHTML = `
            <div class="flex items-center justify-center h-full">
                <div class="text-red-500 text-center">
                    <p class="text-lg font-semibold">Error loading visualization</p>
                    <p class="text-sm text-gray-600">Please check your data.json file</p>
                </div>
            </div>
        `;
    }
};




// Initialize the network when the document is ready
document.addEventListener('DOMContentLoaded', initializeNetwork);