# Lattice Gas Simulation
## Introduction
College project simulating gas diffusion by the movements of individual particles.
Lattice Gas Cellular Automata (LGCA) are used to simulate fluid flows or gas behaviour.

## Step by step
The simulation consists of two main operations:
* Streaming operation with consideration of wall reflections
* Collision operation

After implementing algorithmic part of the simulation, I added thread pools to increase performance and some simple operations to display the gas behaviour. 

### The final result
* All particles
<p align="center">
    <img src="./output/gas_simulation_gif.gif">
</p>

* Calculating concentration
<p align="center">
    <img src="./output/gas_sim_w_concentration_gif.gif">
</p>

## Sources and inspirations
* https://en.wikipedia.org/wiki/Lattice_gas_automaton