package net.fabricmc.fabric.test.object.builder;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.command.arguments.ColorArgumentType.color;
import static net.minecraft.command.arguments.ColorArgumentType.getColor;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.text.FabricStyle;

import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class FabricStyleTests implements ModInitializer {
	private static final Map<UUID, FabricStyle> TRACKED_STYLES = new HashMap<>();

	@Override
	public void onInitialize() {
		final Style empty = Style.EMPTY;
		System.out.println(empty);
		System.out.println(empty.getFont());

		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
			final RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();
			final LiteralCommandNode<ServerCommandSource> literal = dispatcher.register(literal("fabric_text_tests"));

			root.addChild(literal);

			final LiteralCommandNode<ServerCommandSource> bold = literal("bold").redirect(literal, context -> {
				return this.applySimpleStyleModification(context, style -> style.withBold(true));
			}).build();

			final LiteralCommandNode<ServerCommandSource> italic = literal("italic").redirect(literal, context -> {
				return this.applySimpleStyleModification(context, style -> style.withItalic(true));
			}).build();

			final LiteralCommandNode<ServerCommandSource> underline = literal("underline").redirect(literal, context -> {
				return this.applySimpleStyleModification(context, style -> style.withUnderline(true));
			}).build();

			final LiteralCommandNode<ServerCommandSource> strikethrough = literal("strikethrough").redirect(literal, context -> {
				return this.applySimpleStyleModification(context, style -> style.withStrikethrough(true));
			}).build();

			final LiteralCommandNode<ServerCommandSource> obfuscated = literal("obfuscated").redirect(literal, context -> {
				return this.applySimpleStyleModification(context, style -> style.withObfuscated(true));
			}).build();

			final LiteralCommandNode<ServerCommandSource> color = literal("color").then(argument("color", color()).redirect(literal, context -> {
				return this.applyStyleColorModification(context, getColor(context, "color"));
			})).build();

			final LiteralCommandNode<ServerCommandSource> send = literal("send")
					.then(argument("msg", greedyString()).executes(context -> {
				final ServerPlayerEntity player = context.getSource().getPlayer();
				FabricStyle style = TRACKED_STYLES.remove(player.getUuid());

				if (style == null) {
					style = FabricStyle.empty();
				}

				player.sendMessage(new LiteralText(getString(context, "msg")).setStyle(style), false);

				return 1;
			})).build();

			literal.addChild(bold);
			literal.addChild(italic);
			literal.addChild(send);
			literal.addChild(underline);
			literal.addChild(strikethrough);
			literal.addChild(obfuscated);
			literal.addChild(color);
		});
	}

	private ServerCommandSource applySimpleStyleModification(CommandContext<ServerCommandSource> context, UnaryOperator<FabricStyle> operator) throws CommandSyntaxException {
		final ServerCommandSource source = context.getSource();
		final UUID uuid = source.getPlayer().getUuid();

		final FabricStyle style = TRACKED_STYLES.computeIfAbsent(uuid, id -> FabricStyle.empty());
		TRACKED_STYLES.put(uuid, operator.apply(style));

		return source;
	}

	private ServerCommandSource applyStyleColorModification(CommandContext<ServerCommandSource> context, Formatting color) throws CommandSyntaxException {
		final ServerCommandSource source = context.getSource();
		final UUID uuid = source.getPlayer().getUuid();

		final FabricStyle style = TRACKED_STYLES.computeIfAbsent(uuid, id -> FabricStyle.empty());
		style.withColor(color);
		TRACKED_STYLES.put(uuid, style);

		return source;
	}
}
