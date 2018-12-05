import Text.Read (readMaybe)

trimPlus :: String -> String
trimPlus ('+':rest) = rest
trimPlus s = s

readInt :: String -> Maybe Int
readInt = readMaybe . trimPlus

readLines :: FilePath -> IO [Maybe Int]
readLines = fmap (map readInt) . fmap lines . readFile

instance Semigroup Int where
    (<>) = (+)

instance Monoid Int where
    mempty  = 0
    mappend = (+)



main = do
    frequencies <- readLines "../../src/main/resources/01-input.txt"
    let result = mconcat frequencies
    putStrLn (show result)
