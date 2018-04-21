/**
 * Enum for type of actions that SLR parser can take
 * @author Derrick Lee <derrickdclee@gmail.com>
 *
 */
public enum LRParserAction {
	SHIFT,
	REDUCE,
	ERROR,
	ACCEPT
}
