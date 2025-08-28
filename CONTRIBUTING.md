# Contributing to WorldResetter

Thank you for your interest in contributing to WorldResetter! We welcome contributions from developers of all skill levels.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher  
- Git
- A Minecraft server for testing (Paper 1.21+ recommended)

### Setting Up the Development Environment

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/Sh4dowking/WorldReset.git
   cd WorldResetter
   ```
3. **Build the project**:
   ```bash
   mvn clean package
   ```
4. **Test the plugin** on your development server

## Code Standards

### Code Style

- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **Naming Conventions**: 
  - Classes: `PascalCase`
  - Methods/Variables: `camelCase`
  - Constants: `UPPER_SNAKE_CASE`
  - Packages: `lowercase`

### Documentation

- **JavaDoc**: All public classes and methods must have JavaDoc comments
- **Inline Comments**: Use for complex logic explanations
- **README Updates**: Update documentation for new features

### Architecture Guidelines

- **Separation of Concerns**: Keep managers focused on specific responsibilities
- **Error Handling**: Always include proper error handling and logging
- **Constants**: Use the Constants class for configuration values
- **Logging**: Use LoggerUtil for all logging operations

## Making Changes

### Branch Naming

- **Features**: `feature/description-of-feature`
- **Bug Fixes**: `bugfix/description-of-fix`  
- **Documentation**: `docs/description-of-changes`
- **Refactoring**: `refactor/description-of-changes`

### Commit Messages

Follow the conventional commit format:

```
<type>(<scope>): <description>

[optional body]

[optional footer]
```

**Types:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes
- `refactor`: Code refactoring
- `test`: Adding tests
- `chore`: Maintenance tasks

**Example:**
```
feat(world): add support for custom world deletion patterns

- Added configurable patterns for world cleanup
- Enhanced WorldManager with pattern matching
- Updated documentation with new configuration options

Closes #123
```

### Development Workflow

1. **Create a branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make your changes** following the code standards

3. **Test thoroughly**:
   - Build the plugin: `mvn clean package`
   - Test on a development server
   - Verify all functionality works as expected

4. **Commit your changes**:
   ```bash
   git add .
   git commit -m "feat(scope): description of changes"
   ```

5. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request** on GitHub

## Testing Guidelines

### Manual Testing

- Test all commands with and without permissions
- Test world reset functionality thoroughly
- Verify error handling works correctly
- Test on different server versions if possible

### Test Environment Setup

- Use a dedicated test server
- Back up any important data before testing
- Test with different world configurations
- Verify log files are generated correctly

## Pull Request Process

### Before Submitting

- [ ] Code follows the style guidelines
- [ ] All new code is properly documented
- [ ] Changes have been tested thoroughly
- [ ] Commit messages follow the convention
- [ ] README/documentation updated if needed

### Pull Request Template

When creating a pull request, please include:

1. **Description** of changes made
2. **Type of change** (bug fix, feature, docs, etc.)
3. **Testing** performed and results
4. **Screenshots** if applicable (for UI changes)
5. **Checklist** of completed items

### Review Process

1. **Automated Checks**: Ensure all builds pass
2. **Code Review**: Maintainers will review your code
3. **Testing**: Changes will be tested on various environments
4. **Feedback**: Address any requested changes
5. **Merge**: Approved changes will be merged

## Issue Reporting

### Bug Reports

Include the following information:

- **Plugin Version**: WorldResetter version
- **Server Version**: Minecraft server type and version
- **Java Version**: Java version being used
- **Steps to Reproduce**: Clear steps to reproduce the issue
- **Expected Behavior**: What should have happened
- **Actual Behavior**: What actually happened
- **Logs**: Relevant log files or console output
- **Additional Context**: Any other relevant information

### Feature Requests

Include the following information:

- **Feature Description**: Clear description of the requested feature
- **Use Case**: Why this feature would be useful
- **Proposed Implementation**: Ideas for how it could be implemented
- **Alternatives**: Any alternative solutions considered
- **Additional Context**: Any other relevant information

## Code of Conduct

### Our Standards

- **Be Respectful**: Treat all contributors with respect
- **Be Inclusive**: Welcome newcomers and diverse perspectives
- **Be Constructive**: Provide helpful feedback and suggestions
- **Be Professional**: Maintain a professional tone in all interactions

### Unacceptable Behavior

- Harassment or discrimination of any kind
- Trolling, insulting, or derogatory comments
- Publishing private information without permission
- Any other conduct inappropriate in a professional setting

## Getting Help

### Documentation

- Read the [README.md](README.md) for basic usage
- Check existing [issues](https://github.com/Sh4dowking/WorldReset/issues) for solutions
- Review the [code documentation](src/) for technical details

### Community Support

- **GitHub Issues**: For bug reports and feature requests
- **GitHub Discussions**: For general questions and community support
- **Code Review**: Maintainers are available for code review assistance

### Maintainer Contact

For questions about contributing or the project direction, you can:

- Open an issue with the `question` label
- Start a discussion in GitHub Discussions
- Contact maintainers through GitHub

## Recognition

Contributors are recognized in the following ways:

- **Contributors Section**: Listed in README.md
- **Release Notes**: Contributions mentioned in release notes
- **Special Thanks**: Outstanding contributions acknowledged

Thank you for contributing to WorldResetter! Your efforts help make this plugin better for the entire Minecraft community.
