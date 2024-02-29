

# ShopPuppet

ShopPuppet is an adaptable shopping list app for Android, designed to streamline your shopping, regardless of what you're buying. Whether it's toothpaste, groceries, or household essentials, ShopPuppet allows you to tag each item with the shops you can purchase them from, like your local grocery store or drug store. This unique tagging system ensures that when you're in a specific store, you know exactly what needs to be picked up there.

Adding items is straightforward: if you need toothpaste, and you've already categorized your shops into a hardware store, a grocery store, and a drug store, simply add toothpaste to your list and tag it with the grocery and drug stores (stores where you would find toothpaste). ShopPuppet's shops page will then show you how many items are linked to each shop. Selecting a shop will display all the tagged items for that location, so toothpaste will appear under both the grocery store and drug store but not under the hardware store. Once an item is purchased from any tagged store, it's marked off across all locations, simplifying your shopping experience.

With ShopPuppet, your shopping lists become more organized and tailored, making sure you never forget an item again, no matter where you need to buy it from.

## Features

- **MVVM Architecture**: Built following the Model-View-ViewModel pattern for a robust and maintainable codebase.
- **Room Database**: Utilizes Room for local data persistence, enabling efficient data caching and offline access.
- **Dynamic Shop Tagging**: Allows users to tag items to specific shops, enhancing the shopping experience by organizing items shop-wise.
- **Priority Items**: Users can mark items as priority, helping to focus on essential purchases.
- **LiveData & Kotlin Coroutines**: For responsive and asynchronous data handling.
- **Dagger-Hilt**: Simplifies dependency injection, providing a scalable way to manage object creation.
- **Android Navigation Component**: Manages UI navigation within the app, ensuring a consistent and predictable user experience.
- **Material Design**: Incorporates Material Design components for a modern and cohesive look.
-  **Dark Mode Support**: Offers a built-in dark mode, reducing eye strain in low light conditions and providing a stylish interface option for users who prefer darker color schemes.

## Technologies Used

- Kotlin
- Android Studio
- MVVM Pattern
- Room for local data storage
- Dagger-Hilt for Dependency Injection
- Android Navigation Component
- LiveData & Kotlin Coroutines
- Material Design

## Getting Started

To get a local copy up and running, follow these simple steps:

1. **Clone the repository directly in Android Studio**:
   - Launch Android Studio.
   - On the Welcome screen, choose "Get from Version Control".
   - In the URL field, paste the URL of this repository.
   - Click "Clone".

Alternatively, if you have already launched Android Studio:

- Choose "File" -> "New" -> "Project from Version Control".
- In the URL field, paste the URL of this repository.
- Click "Clone".

Android Studio should automatically handle the Gradle sync process and set up your project after the cloning is complete.


## Running the App

After syncing, you can run the app on an Android emulator or physical device by selecting 'Run' -> 'Run 'app'' from the menu or pressing the green play button in the toolbar.

## Contributing to ShopPuppet

Your feedback and contributions are highly valued! Whether you have suggestions for improvements, have encountered bugs, or want to directly contribute to the codebase, here’s how you can get involved:

- **Reporting Bugs or Suggesting Enhancements**: Noticed something that doesn't work as expected or have an idea for a new feature? Head over to the [Issues](https://github.com/thatwaz/ShopPuppet/issues) section of our GitHub repository to report bugs or suggest enhancements.
- **Making Contributions**: Ready to add your own code or documentation? Great! Please submit a pull request through the [Pull Requests](https://github.com/thatwaz/ShopPuppet/pulls) section. If you're not sure how to create a pull request, GitHub’s [Creating a pull request](https://help.github.com/en/articles/creating-a-pull-request) guide is a great resource.

For more detailed information on how to contribute, please take a look at our [Contribution Guidelines](https://github.com/thatwaz/ShopPuppet/blob/master/CONTRIBUTING.md). We look forward to your contributions and are excited to see how we can improve ShopPuppet together!






## Credits

- **UI Design and Icons**: Designed with Material Design guidelines for a cohesive and intuitive user experience.
- **Data Handling**: Room and LiveData are used for efficient data management and UI updates.
- **Bullseye Icon**: Sourced from [Iconfinder](https://www.iconfinder.com). The icon has been resized to fit the app's design, with an adjustment of 12dp to its original size. For more details on the licensing and usage terms, please visit [Iconfinder's License Overview](https://support.iconfinder.com/en/articles/18233-license-overview).


## Licenses

### Project License

This project is open-source and available under the [MIT License](LICENSE).

### Third-Party Acknowledgments

- **Material Icons**: Icons used within this app are provided by the Material Icons library, which is part of the Material Design system by Google. These icons are licensed under the [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.txt).





   
