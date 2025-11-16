# Bossku - Point of Sale (POS) System

A modern Android POS application built with Kotlin for managing items, categories, tickets, and transaction history. Designed for small businesses to handle sales transactions efficiently.

## 📱 Features

### Core Functionality
- **Item Management**: Create, edit, delete items with customizable colors, pricing, and categories
- **Category Management**: Organize items into color-coded categories
- **Ticket System**: Create and manage sales tickets with multiple items
- **Transaction History**: Track all paid transactions with search and sorting
- **Multi-language Support**: English, Bahasa Melayu (Malay), and Chinese (中文)
- **Theme Support**: Light and dark mode

### User Interface
- **Home Screen**: Grid view of items/categories with drag-and-drop reordering
- **Shopping Cart**: Temporary ticket system for building orders
- **Search & Filter**: Real-time search across items, categories, and tickets
- **Sort Options**: Sort by name or date/time, ascending or descending
- **Color Coding**: Visual color indicators for items and categories with selection feedback

### Advanced Features
- **Persistent Cart**: Cart items persist across language changes and app restarts
- **Barcode Support**: Optional barcode field for items
- **Cost Tracking**: Track both selling price and cost per item
- **Quantity Management**: Edit quantities in tickets
- **Auto-redirect**: Tickets auto-navigate after payment (10-second timer)

## 🏗️ Architecture

### Tech Stack
- **Language**: Kotlin
- **Min SDK**: 28 (Android 9.0)
- **Target SDK**: 36
- **Architecture**: MVVM (Model-View-ViewModel)

### Libraries & Dependencies
- **Room Database**: Local data persistence with TypeConverters
- **Kotlin Coroutines**: Asynchronous operations with Flow
- **Navigation Component**: Single-activity architecture with SafeArgs
- **ViewBinding**: Type-safe view access
- **DataStore**: Preferences storage (theme and grid ordering)
- **Material Design 3**: Modern UI components
- **RecyclerView**: Efficient list rendering with ItemTouchHelper for drag-and-drop

### Database Schema
- **Ticket**: Sales transactions (id, name, createdAt, paidAt, status, total)
- **TicketDetail**: Line items in tickets (ticket-item relationship)
- **Item**: Products for sale (name, category, price, cost, barcode, color)
- **Category**: Item categorization (name, color)

## 📂 Project Structure

```
app/src/main/java/com/team/bossku/
├── data/
│   ├── db/           # Room DAOs and Database
│   ├── ds/           # DataStore (Theme, Grid)
│   ├── model/        # Entity classes
│   └── repo/         # Repository layer
├── ui/
│   ├── home/         # Main screen (items/categories grid)
│   ├── ticket/       # Saved tickets list
│   ├── ticket_detail/# Ticket details with payment
│   ├── history/      # Paid transactions
│   ├── settings/     # App settings
│   ├── manage/       # Add/Edit items and categories
│   ├── list/         # Management lists
│   ├── adapter/      # RecyclerView adapters
│   └── popup/        # Dialog fragments
├── MainActivity.kt   # Single activity host
├── MyApp.kt         # Application class
└── LangManager.kt   # Language switching utility
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 11 or higher
- Android SDK 28+

### Installation
1. Clone the repository
   ```bash
   git clone https://github.com/Jia0705/Bossku.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files

4. Run the app on an emulator or physical device

## 💡 Usage

### Creating Items
1. Navigate to **Settings** → **Manage Items**
2. Click **+** to add new item
3. Enter name, price, cost, category, and select color
4. Save to create the item

### Creating Tickets
1. From **Home**, tap items to add to cart
2. Click **Cart** icon → Enter ticket name → **Save**
3. View saved tickets in **Ticket List**

### Processing Sales
1. Open a saved ticket from **Ticket List**
2. Edit quantities or remove items if needed
3. Click **Pay** to complete transaction
4. View in **History** after payment

### Customization
- **Settings** → Change language, theme, or manage data
- **Long-press** Home toolbar to switch between items/categories view
- **Drag & drop** items/categories to reorder
- **Search bar** for quick filtering

## 🔧 Configuration

### Language Support
- English (default)
- Bahasa Melayu (`ms`)
- Chinese - 中文 (`zh`)

### Theme Options
- Light mode
- Dark mode (system-dependent)

## 📄 License

This project is developed as an academic/learning project.

## 👥 Contributors

- Jia0705 (GitHub)

## 🔗 Links

- Repository: [https://github.com/Jia0705/Bossku](https://github.com/Jia0705/Bossku)

---

**Last Updated**: November 16, 2025
