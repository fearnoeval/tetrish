# Tetrish

- Similar to Tetris®; Tetris-ish; Tetrish
- Written in ClojureScript with core.async

## Where to play

- [https://hacks.fearnoeval.com/tetrish/](https://hacks.fearnoeval.com/tetrish/)

## How to play

- `↑`: Rotate
- `↓`: Soft drop
- `←`: Left
- `→`: Right
- `S`: Save
  - A prompt will pop up that has the full game state as JSON in the input field
  - Game state can be copied out, saved for later, investigated, modified, etc.
- `L`: Load
  - Prompts for a saved game state
  - Saved game state must be in the same format as that created via `S`
  - Invalid game states are ignored, continuing the game already in progress
- `F5`: New Game

## How to build and run locally

1. Install [Leiningen](https://leiningen.org/)
2. Run `lein cljsbuild once`
3. Open `resources/index.html` in your web browser of choice

## Notes

- The game is fully data-driven, in that all of the game state is stored in a
  single map
- Rotation behaves the same as NES Tetris, not how later versions behave
- As an experiment, DOM string generation and insertion for the entire game UI
  happens on every state change
  - I was curious if this would be performant enough, and for something this
    basic, it is (tested using Chrome 47+)
- Includes a basic, protocol-based, ClojureScript version of Hiccup
- Written in January 2016

## Bugs as features

- "Where's the next piece?"
  - Marketing: Tetrish is only available in hard mode
  - Truth: I forgot
- "How do I hard drop?"
  - Marketing: Tetrish is only available in hard mode
  - Truth: I forgot
- "Why can't I play on mobile?"
  - Marketing: Playing Tetrish on mobile enables infinitely-hard mode
  - Truth: I did a quick attempt at this, but getting swipe up, down, left, and
    right to control the game instead of scrolling was not working so well

## License

- © 2016-2017 [Tim Walter](https://www.fearnoeval.com/)
- Licensed under the [Eclipse Public License 1.0](LICENSE.html)
- Tetris is a registered trademark of Tetris Holding
