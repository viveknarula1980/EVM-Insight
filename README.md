# EVM Insight

**EVM Insight** is an advanced and interactive reference tool that helps developers, auditors, and blockchain enthusiasts explore the inner workings of the Ethereum Virtual Machine (EVM). Built as a modern, developer-friendly extension of , EVM Insight provides in-depth opcode analysis, contract disassembly, and educational tools for understanding low-level Ethereum logic.

---

## 🔍 Features

- 🧠 **Comprehensive EVM Opcode Reference**
  - Stack behavior, gas cost, and descriptions
  - Real-time example simulations
- 🔬 **Bytecode Disassembler**
  - Paste smart contract bytecode and visualize EVM instructions
- 🧪 **Interactive EVM Playground**
  - Simulate and step through execution of raw bytecode
- 📊 **Instruction Metadata**
  - Includes historical changes (e.g., hardfork gas adjustments)
- 🎓 **Educational Focus**
  - Designed for smart contract learners, researchers, and auditors

---

## 🚫 Not a Wallet

EVM Insight is **not a wallet** and does **not**:
- Store private keys
- Connect to any blockchain or Web3 providers
- Interact with your MetaMask or other wallets

This tool is strictly for **offline exploration and visualization** of EVM behavior.

---



## 🛠 Tech Stack

- **Framework**: Vue 3 + Vite
- **Language**: TypeScript
- **UI**: Tailwind CSS
- **Tooling**: PNPM, EVM bytecode interpreter

---

## 💻 Getting Started

```bash
# Clone the repository
cd evm-insight

# Install dependencies
pnpm install

# Start the development server
pnpm dev
